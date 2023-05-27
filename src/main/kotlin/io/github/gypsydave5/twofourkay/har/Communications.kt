package io.github.gypsydave5.twofourkay.har

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName
import io.github.gypsydave5.twofourkay.generateKotlin.asCodeBlock

class Communications(comms: List<Pair<org.http4k.core.Request, org.http4k.core.Response>>) :
    List<Communication> by (comms.map {
        Communication(
            it.first,
            it.second
        )
    })

data class Communication(val request: org.http4k.core.Request, val response: org.http4k.core.Response)

fun HAR.toCommunications(): Communications {
    return Communications(log.entries.map { entry: Entry -> entry.request.toRequest() to entry.response.toResponse() })
}

private fun Entry.toCommunication() = Communication(this.request.toRequest(), this.response.toResponse())
private fun Request.toRequest() = org.http4k.core.Request.parseHar(this)
private fun Response.toResponse() = org.http4k.core.Response.parseHar(this)

fun Communications.generateKotlin(): String {

    return foldIndexed(FileSpec.builder("", "generated")) { idx, fsb, comm ->
        fsb.addProperty(
            PropertySpec.builder("request${idx}", org.http4k.core.Request::class.asTypeName())
                .initializer(comm.request.asCodeBlock())
                .build()
        )
            .addProperty(
                PropertySpec.builder("response${idx}", org.http4k.core.Response::class.asTypeName())
                    .initializer(comm.response.asCodeBlock())
                    .build()
            )
    }.build().toString()
}