package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.*
import org.http4k.core.Response

fun Response.generateKotlin(): String {
    val base = asCodeBlock()

    return FileSpec.builder("", "generated")
        .addProperty(
            PropertySpec.Companion.builder("response", Response::class.asTypeName())
                .initializer(base)
                .build()
        )
        .build()
        .toString()
}

fun Response.asCodeBlock(): CodeBlock = CodeBlock.builder()
    .add(
        """%T(%L%L)""",
        Response::class.asClassName(),
        status.toCodeBlock(),
        httpVersionCodeBlock(version)
    )
    .add(headers.headersCodeBlock())
    .add(body)
    .build()