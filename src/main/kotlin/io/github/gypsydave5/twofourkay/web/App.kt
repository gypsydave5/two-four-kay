package io.github.gypsydave5.twofourkay.web

import Configuration
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.template.HandlebarsTemplates

class App(config: Configuration) : HttpHandler {
    override fun invoke(request: Request): Response {
        return routing(request)
    }

    val renderer = HandlebarsTemplates().HotReload("src/main/resources/templates")

    private val staticHandler = static(config.publicResources)


    private val routing = routes(
        "/har" bind harHandler,
        "/" bind Method.POST to ParseHandler(renderer),
        "/" bind Method.GET to staticHandler
    )
//        .withFilter(htmxFilter)
}





