package io.github.gypsydave5.twofourkay.web

import dev.forkhandles.result4k.asResultOr
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.parse.har.HAR
import io.github.gypsydave5.twofourkay.parse.har.parseHar
import io.github.gypsydave5.twofourkay.parse.har.toHttpTransactions
import org.http4k.core.HttpTransaction
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes

val harHandler = routes(
    Method.POST bind { request ->
        request.bodyString().parseHar()
            .map(HAR::toHttpTransactions)
            .map(List<HttpTransaction>::generateKotlin)
            .map { Response(Status.OK).body(it) }
            .recover { Response(Status.BAD_REQUEST).body(it.toString()) }
    },

    Method.GET bind { request ->
        request.query("har")
            .asResultOr { IllegalArgumentException("Missing har parameter in query string") }
            .flatMap { it.parseHar() }
            .map(HAR::toHttpTransactions)
            .map(List<HttpTransaction>::generateKotlin)
            .map { Response(Status.OK).body(it) }
            .recover { Response(Status.BAD_REQUEST).body(it.message ?: "unknown error") }
    }
)
