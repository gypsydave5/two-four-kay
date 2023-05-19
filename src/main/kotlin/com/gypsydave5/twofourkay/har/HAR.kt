package com.gypsydave5.twofourkay.har

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

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
    val path: String,
    val domain: String,
    val expires: String,
    val httpOnly: Boolean,
    val secure: Boolean,
)

@Serializable
data class Log(
    val version: String,
    val creator: Creator,
    val browser: Browser,
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
    val onContentLoad: Long,
    val onLoad: Long,
)

@Serializable
data class Entry(
    val pageref: String,
    val startedDateTime: String,
    val request: Request,
    val response: Response,
    val cache: Cache,
    val timings: Timings,
    val time: Long,
    @JsonNames("securityState", "_securityState") val securityState: String,
    @JsonNames("serverIpaddress", "serverIPAddress") val serverIpaddress: String,
    val connection: String,
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
)

@Serializable
data class Content(
    val mimeType: String,
    val size: Long,
    val encoding: String? = null,
    val text: String,
)

@Serializable
data class Timings(
    val blocked: Long,
    val dns: Long,
    val connect: Long,
    val ssl: Long,
    val send: Long,
    val wait: Long,
    val receive: Long,
)
