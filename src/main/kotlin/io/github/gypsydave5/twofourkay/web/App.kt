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
    private val renderer = Renderer(config)
    private val staticHandler = PublicHttpHandler(config)

    override fun invoke(request: Request): Response {
        return routing(request)
    }


    private val routing = routes(
        "/har" bind harHandler,
        "/" bind Method.POST to ParseHandler(renderer),
        "/" bind Method.GET to staticHandler
    )
}

class Renderer(config: Configuration) : TemplateRenderer by if (config.env == "dev") {
    HandlebarsTemplates().HotReload("src/main/resources/templates")
} else {
    HandlebarsTemplates().CachingClasspath("public")
}

class PublicHttpHandler(config: Configuration) : HttpHandler by if (config.env == "dev") {
    static(ResourceLoader.Directory("src/main/resources/public"))
} else {
    static(ResourceLoader.Classpath("/public"))
}






