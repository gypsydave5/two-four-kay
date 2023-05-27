package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.CodeBlock
import org.http4k.core.Body

internal fun Body.toCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()
    String(payload.array()).takeIf(String::isNotEmpty)
        ?.unescapePercents()
        ?.also { base.add("\n\t.body(${it.tripleQuote()})") }
    return base.build()
}

internal fun CodeBlock.Builder.add(body: Body): CodeBlock.Builder = add(body.toCodeBlock())