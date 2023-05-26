package com.gypsydave5.twofourkay.har

import org.http4k.core.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.body.form


fun Request.Companion.parseHar(harRequest: com.gypsydave5.twofourkay.har.Request): Request = Request(
    method = Method.valueOf(harRequest.method),
    uri = harRequest.url,
    version = harRequest.httpVersion.toHttpVersion(),
)
    .headers(harRequest.headers.map(Header::toPair))
    .let { request -> harRequest.postData?.params?.fold(request) { r, p -> r.form(p.name, p.value) } ?: request }
    .let { request -> harRequest.postData?.text?.let { text -> request.body(text) } ?: request }


fun Response.Companion.parseHar(harResponse: com.gypsydave5.twofourkay.har.Response): Response = Response(
    status = Status(harResponse.status.toInt(), harResponse.statusText),
    version = harResponse.httpVersion.toHttpVersion(),
)
    .headers(harResponse.headers.map(Header::toPair))
    .body(harResponse.content.text ?: "")

private fun Header.toPair(): Pair<String, String> {
    return when (name.lowercase()) {
        "set-cookie" -> name to value
        else -> name to value
    }
}

private fun String.toHttpVersion(): String {
    return when (uppercase()) {
        "HTTP/2", "H3", "H2" -> HttpMessage.HTTP_2
        else -> HttpMessage.HTTP_1_1
    }
}