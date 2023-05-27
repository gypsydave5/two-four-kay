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
            """%T(""",
            Response::class.asClassName(),
        )
        .add(status.toCodeBlock())
        .add(httpVersionCodeBlock(version))
        .add(")")

    headers.forEach {
        base.add("\n\t.header(\"\"\"${it.first}\"\"\", \"\"\"${it.second?.unescapePercents()?.trim('\"')}\"\"\")")
    }

    bodyString().takeIf(String::isNotEmpty)
        ?.unescapePercents()
        ?.also { base.add("\n\t.body(\"\"\"$it\"\"\")") }
    return base.build()
}

