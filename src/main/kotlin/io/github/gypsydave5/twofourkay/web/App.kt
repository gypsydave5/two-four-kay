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
import org.http4k.template.TemplateRenderer

class App(config: Configuration) : HttpHandler {
    private val renderer = newRenderer(config.env)
    private val staticHandler = static(config.publicResources)

    override fun invoke(request: Request): Response {
        return routing(request)
    }


    private val routing = routes(
        "/har" bind harHandler,
        "/" bind Method.POST to ParseHandler(renderer),
        "/" bind Method.GET to staticHandler
    )
}

private fun newRenderer(env: String): TemplateRenderer {
    if (env == "dev") {
        return HandlebarsTemplates().HotReload("src/main/resources/templates")
    } else {
        return HandlebarsTemplates().CachingClasspath()
    }
}






