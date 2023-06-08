package io.github.gypsydave5.twofourkay.curl

import CurlBaseListener
import CurlParser
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri

internal class CurlListener : CurlBaseListener() {
    private var request: Request = Request(Method.GET, "")

    override fun enterUrl(ctx: CurlParser.UrlContext) {
        request = request.uri(Uri.of(ctx.text.stripQuotes()))
    }

    override fun enterOption(ctx: CurlParser.OptionContext) {
        val optionName = ctx.optionName().text
        val optionValue = ctx.optionValue()?.text?.stripQuotes()

        request = when (optionName) {
            "-X", "--request" -> optionValue?.let { request.method(Method.valueOf(it)) }
            "-H", "--header" -> optionValue
                ?.split(':', ignoreCase = false, limit = 2)
                ?.let { request.header(it.first().trim(), it[1].trim()) }

            "-d", "--data", "--data-raw" -> optionValue?.let { request.body(it) }
            else -> null
        } ?: request
    }

    fun buildRequest(): Request = request
}

private fun String.stripQuotes(): String = when (first()) {
    '\'' -> removeSurrounding("\'")
    '\"' -> removeSurrounding("\"")
    else -> this
}