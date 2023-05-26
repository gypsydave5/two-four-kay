package com.gypsydave5.twofourkay.har

class Communications(private val comms: List<Communication>) : List<Communication> by comms

data class Communication(val request: org.http4k.core.Request, val response: org.http4k.core.Response)

fun HAR.toCommunications(): Communications {
    return Communications(log.entries.map { entry: Entry -> entry.toCommunication() })
}

private fun Entry.toCommunication() = Communication(this.request.toRequest(), this.response.toResponse())
private fun Request.toRequest() = org.http4k.core.Request.parseHar(this)
private fun Response.toResponse() = org.http4k.core.Response.parseHar(this)

