@file:OptIn(ExperimentalSerializationApi::class)

package com.gypsydave5.twofourkay.har

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames

fun String.parseHar(): HAR {
    return Json.decodeFromString<HAR>(this)
}

@Serializable
data class HAR(
    val log: Log,
)

@Serializable
data class Header(
    val name: String,
    val value: String,
)

@Serializable
data class Cookie(
    val name: String,
    val value: String,
    val path: String? = null,
    val domain: String? = null,
    val expires: String? = null,
    val httpOnly: Boolean? = null,
    val secure: Boolean? = null,
    val sameSite: String? = null,
)

@Serializable
data class Log(
    val version: String,
    val creator: Creator,
    val browser: Browser? = null,
    val pages: List<Page>,
    val entries: List<Entry>,
)

@Serializable
data class Creator(
    val name: String,
    val version: String,
)

@Serializable
data class Browser(
    val name: String,
    val version: String,
)

@Serializable
data class Page(
    val startedDateTime: String,
    val id: String,
    val title: String,
    val pageTimings: PageTimings,
)

@Serializable
data class PageTimings(
    val onContentLoad: Double,
    val onLoad: Double,
)

@Serializable
data class Entry(
    val pageref: String,
    val startedDateTime: String,
    val request: Request,
    val response: Response,
    val cache: Cache,
    val timings: Timings,
    val time: Double,
    @JsonNames("securityState", "_securityState") val securityState: String? = null,
    @JsonNames("serverIpaddress", "serverIPAddress") val serverIpaddress: String? = null,
    val connection: String? = null,
    @JsonNames("_initiator") val initiator: Initiator? = null,
    @JsonNames("_priority") val priority: String? = null,
    @JsonNames("_resourceType") val resourceType: String? = null,
)

@Serializable
data class Initiator(
    val type: String,
    val url: String? = null,
    val lineNumber: Int? = null
)

@Serializable
data class Cache(
    val beforeRequest: CacheData? = null,
    val afterRequest: CacheData? = null,
)

@Serializable
data class CacheData(
    val expires: String?,
    val lastAccess: String,
    val eTag: String,
    val hitCount: Long,
    val comment: String?,
)


@Serializable
data class Request(
    val bodySize: Long,
    val method: String,
    val url: String,
    val httpVersion: String,
    val headers: List<Header>,
    val cookies: List<Cookie>,
    val queryString: List<QueryString>,
    val headersSize: Long,
    val postData: PostData? = null
)

@Serializable
data class PostData(
    val mimeType: String,
    val text: String,
    val params: List<Param>,
)

@Serializable
data class Param(
    val name: String,
    val value: String,
)

@Serializable
data class QueryString(
    val name: String,
    val value: String,
)

@Serializable
data class Response(
    val status: Long,
    val statusText: String,
    val httpVersion: String,
    val headers: List<Header>,
    val cookies: List<Cookie>,
    val content: Content,
    val redirectURL: String,
    val headersSize: Long,
    val bodySize: Long,
    @JsonNames("_transferSize") val transferSize: Long? = null,
    @JsonNames("_error") val error: Long? = null,
)

@Serializable
data class Content(
    val mimeType: String? = null,
    val size: Long? = null,
    val encoding: String? = null,
    val text: String? = null,
    val comment: String? = null
)

@Serializable
data class Timings(
    val blocked: Double? = null,
    val dns: Long? = null,
    val connect: Long? = null,
    val ssl: Long? = null,
    val send: Double? = null,
    val wait: Double? = null,
    val receive: Double? = null,
    @JsonNames("_blocked_queueing") val blocked_queueing: Double? = null,
)
