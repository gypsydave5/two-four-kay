package io.github.gypsydave5.twofourkay.web

import Configuration
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.TemplateRenderer

class App(config: Configuration) : HttpHandler {
    private val renderer = handlebarsTemplateRenderer(config)
    private val staticHandler = publicHttpHandler(config)

    override fun invoke(request: Request): Response {
        return routing(request)
    }

    private val routing = routes(
        "/har" bind HarHandler(),
        "/" bind Method.POST to ParseHandler(renderer),
        "/" bind Method.GET to staticHandler
    )
}

fun handlebarsTemplateRenderer(config: Configuration): TemplateRenderer = if (config.env == "dev") {
    HandlebarsTemplates().HotReload("src/main/resources/templates")
} else {
    HandlebarsTemplates().CachingClasspath("templates")
}

fun publicHttpHandler(config: Configuration): HttpHandler = if (config.env == "dev") {
    static(ResourceLoader.Directory("src/main/resources/public"))
} else {
    static(ResourceLoader.Classpath("public"))
}






