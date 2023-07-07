package io.github.gypsydave5.twofourkay.parse.sniffer

import org.http4k.core.Method

fun sniff(content: String): ContentType {
    if (content.isWireRequest()) {
        return ContentType.WireRequest
    }

    if (content.isWireResponse()) {
        return ContentType.WireResponse
    }

    if (content.isCurl()) {
        return ContentType.Curl
    }

    if (content.startsWith("{")) {
        return ContentType.HAR
    }

    return ContentType.Unknown
}

private fun String.isCurl() = startsWith("curl")

private fun String.isWireRequest(): Boolean {
    val methodString = split(" ").firstOrNull() ?: return false

    return try {
        Method.values().contains(Method.valueOf(methodString))
    } catch (_: Exception) {
        false
    }
}


private fun String.isWireResponse(): Boolean {
    val httpVersionString = split(" ").firstOrNull() ?: return false

    return httpVersionString.startsWith("HTTP")
}

enum class ContentType {
    HAR,
    Curl,
    WireRequest,
    WireResponse,
    Unknown
}