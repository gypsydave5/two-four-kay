package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.queries

fun Request.generateKotlin(): String {
    val base = asCodeBlock()

    return FileSpec.builder("", "generated")
        .addProperty(
            PropertySpec.Companion.builder("request", Request::class.asTypeName())
                .initializer(base)
                .build()
        )
        .build()
        .toString()
}

fun Request.asCodeBlock(): CodeBlock = CodeBlock.builder().add(
    "%T(%T.$method,·%S%L)",
    Request::class.asClassName(),
    Method::class.asClassName(),
    uri.toString().removeSuffix("?" + uri.query),
    httpVersionCodeBlock(version)
)
    .add(uri.queries().queryCodeBlock())
    .add(headers.headersCodeBlock())
    .add(body)
    .build()