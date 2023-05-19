package com.gypsydave5.twofourkay.har

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response

import kotlinx.serialization.json.*
import org.http4k.core.Status


fun Request.Companion.parseHar(harRequest: com.gypsydave5.twofourkay.har.Request): Request {
    return Request(
        Method.valueOf(harRequest.method),
        harRequest.url,
        version = harRequest.httpVersion.uppercase(),
    ).headers(harRequest.headers.map { it.name to it.value })
}

fun Response.Companion.parseHar(res: com.gypsydave5.twofourkay.har.Response): Response {
    val response = Response(
        Status(res.status.toInt(), res.statusText)
    )
        .headers(res.headers.map { it.name to it.value })

    res.content.text?.let { response.body(it) }

    return response
}

fun String.parseHar(): HAR {
    return Json.decodeFromString<HAR>(this)
}