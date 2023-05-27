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

fun Request.asCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()

    base.add(
        "%T(%T.$method,·%S%L)",
        Request::class.asClassName(),
        Method::class.asClassName(),
        uri.toString().removeSuffix("?" + uri.query),
        httpVersionCodeBlock(version)
    )
//        .add(httpVersionCodeBlock(version))
//        .add(")")

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