package io.github.gypsydave5.twofourkay.web

import org.http4k.core.HttpHandler
import org.http4k.core.Status

val htmxFilter: (HttpHandler) -> HttpHandler = { handler: HttpHandler ->
    val handler: HttpHandler = { request: org.http4k.core.Request ->
        val response = handler(request)
        if (response.status.clientError || response.status.serverError) {
            response
                .status(Status.OK)
                .header("HX-Retarget", "#errors")
                .header("HX-Reswap", "innerHTML");
        } else {
            response
        }
    }
    handler
}