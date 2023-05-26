package io.github.gypsydave5.twofourkay

import com.squareup.kotlinpoet.*
import org.http4k.core.*

fun Request.generateKotlin(): String {
    val base = asCodeBlock()

    return FileSpec.builder("", "generated")
        .addProperty(
            PropertySpec.builder("request", Request::class.asTypeName())
                .initializer(base)
                .build()
        )
        .build()
        .toString()
}

fun Response.generateKotlin(): String {
    val base = asCodeBlock()

    return FileSpec.builder("", "generated")
        .addProperty(
            PropertySpec.builder("response", Request::class.asTypeName())
                .initializer(base)
                .build()
        )
        .build()
        .toString()
}

fun Response.asCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()
        .add(
            """return %T(%T(${status.code}, ""))""",
            Response::class.asClassName(),
            Status::class.asClassName()
        )

    headers.forEach {
        base.add("\n\t.header(\"\"\"${it.first}\"\"\", \"\"\"${it.second?.unescapePercents()?.trim('\"')}\"\"\")")
    }

    bodyString().takeIf(String::isNotEmpty)
        ?.unescapePercents()
        ?.also { base.add("\n\t.body(\"\"\"$it\"\"\")") }
    return base.build()
}

fun Request.asCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()
        .add(
            "return %T(%T.$method,·%S)",
            Request::class.asClassName(),
            Method::class.asClassName(),
            uri.toString().removeSuffix("?" + uri.query)
        )

    uri.queries().forEach {
        base.add("\n\t.query(\"\"\"${it.first}\"\"\", \"\"\"${it.second?.unescapePercents()?.trim('\"')}\"\"\")")
    }

    headers.forEach {
        base.add("\n\t.header(\"\"\"${it.first}\"\"\", \"\"\"${it.second?.unescapePercents()?.trim('\"')}\"\"\")")
    }

    bodyString().takeIf {
        it.isNotEmpty()
    }?.unescapePercents()?.also { base.add("\n\t.body(\"\"\"$it\"\"\")") }
    return base.build()
}

private fun String.unescapePercents() = replace("%", "%%")