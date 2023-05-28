package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.CodeBlock
import org.http4k.core.Headers
import org.http4k.core.Parameters

internal fun Headers.headersCodeBlock() = toCodeBlock("header")
internal fun Parameters.queryCodeBlock() = toCodeBlock("query")
internal fun List<Pair<String, String?>>.toCodeBlock(kind: String): CodeBlock {
    val base = CodeBlock.builder()
    forEach {
        base.add("\n\t.$kind(%S, %S)", it.first, it.second?.unescapePercents()?.trim('\"'))
    }
    return base.build()
}