package com.gypsydave5.twofourkay.har

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status


fun Request.Companion.parseHar(harRequest: com.gypsydave5.twofourkay.har.Request): Request = Request(
    method = Method.valueOf(harRequest.method),
    uri = harRequest.url,
    version = harRequest.httpVersion.uppercase(),
)
    .headers(harRequest.headers.map(Header::toPair))

fun Response.Companion.parseHar(harResponse: com.gypsydave5.twofourkay.har.Response): Response = Response(
    status = Status(harResponse.status.toInt(), harResponse.statusText),
    version = harResponse.httpVersion.uppercase(),
)
    .body(harResponse.content.text ?: "")
    .headers(harResponse.headers.map(Header::toPair))

private fun Header.toPair(): Pair<String, String> {
    return when (name.lowercase()) {
        "set-cookie" -> name to value.fixSameSite()
        else -> name to value
    }
}

private fun String.fixSameSite(): String {
    return replace("; SameSite=(lax|LAX)".toRegex(), "; SameSite=Lax")
}