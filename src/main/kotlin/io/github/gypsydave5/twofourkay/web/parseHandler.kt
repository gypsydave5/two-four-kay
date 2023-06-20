package io.github.gypsydave5.twofourkay.web

import dev.forkhandles.result4k.asResultOr
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.recover
import io.github.gypsydave5.twofourkay.parse.parse
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
import org.http4k.template.TemplateRenderer

class ParseHandler(private val renderer: TemplateRenderer) : HttpHandler {
    override fun invoke(request: Request): Response {
        return request.form("content")
            .asResultOr { Error("missing content field") }
            .flatMap { parse(it) }
            .map { ParseResult(it) }
            .map { Response(Status.OK).body(renderer(it)) }
            .recover { Response(Status.BAD_REQUEST).body(renderer(ErrorResult(it.message))) }
    }
}
