package io.github.gypsydave5.twofourkay.parse.curl

import CurlBaseListener
import CurlParser
import org.http4k.core.*
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.HttpMessage.Companion.HTTP_2

internal class CurlListener : CurlBaseListener() {
    var version: String? = null
    var method: Method = Method.GET
    var headers: Headers = listOf()
    var body: Body = Body("")
    var uri = Uri.of("")

    override fun enterUrl(ctx: CurlParser.UrlContext) {
        uri = (Uri.of(ctx.text.stripQuotes()))
    }

    override fun enterOption(ctx: CurlParser.OptionContext) {
        val optionName = ctx.optionName().text
        val optionValue = ctx.optionValue()?.text?.stripQuotes()

        when (optionName) {
            "-X", "--request" -> optionValue?.let { method = Method.valueOf(it) }
            "-H", "--header" -> optionValue
                ?.split(':', ignoreCase = false, limit = 2)
                ?.let { headers += (it.first().trim() to it[1].trim()) }

            "-d", "--data", "--data-raw" -> optionValue?.let { body = Body(it) }
            "--http2" -> version = HTTP_2
        }
    }

    fun buildRequest(): Request = Request(method, uri, version ?: HTTP_1_1).headers(headers).body(body)
}

private fun String.stripQuotes(): String = when (first()) {
    '\'' -> removeSurrounding("\'")
    '\"' -> removeSurrounding("\"")
    else -> this
}