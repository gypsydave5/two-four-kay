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

fun Response.asCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()
        .add(
            """%T(%L%L)""",
            Response::class.asClassName(),
            status.toCodeBlock(),
            httpVersionCodeBlock(version)
        )

    headers.forEach {
        base.add("\n\t.header(${it.first.tripleQuote()}, ${it.second?.unescapePercents()?.trim('\"')?.tripleQuote()})")
    }

    bodyString().takeIf(String::isNotEmpty)
        ?.unescapePercents()
        ?.also { base.add("\n\t.body(${it.tripleQuote()})") }
    return base.build()
}

