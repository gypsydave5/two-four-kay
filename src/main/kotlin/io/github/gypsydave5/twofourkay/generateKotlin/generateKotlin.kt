package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import org.http4k.core.HttpMessage

internal fun String.unescapePercents() = replace("%", "%%")

internal fun httpVersionCodeBlock(version: String): CodeBlock {
    val base = CodeBlock.builder()
    if (version != HttpMessage.HTTP_1_1) {
        base.add(", %T.HTTP_2", HttpMessage::class.asClassName())
    }

    return base.build()
}

internal fun String.tripleQuote(): String = "\"\"\"${this}\"\"\""

