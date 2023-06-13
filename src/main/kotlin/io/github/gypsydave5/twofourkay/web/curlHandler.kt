package io.github.gypsydave5.twofourkay.web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes

val curlHandler = routes(
    Method.POST bind { request ->
        request.bodyString()
            .parseCurl()
            .generateKotlin()
            .let { Response(Status.OK).body(it) }
    },
)
