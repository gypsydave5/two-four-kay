package io.github.gypsydave5.twofourkay.parse.curl

import CurlLexer
import CurlParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.http4k.core.Request

fun Request.Companion.parseCurl(curl: String): Request {
    val input = CharStreams.fromString(curl)
    val lexer = CurlLexer(input)

    val tokens = CommonTokenStream(lexer)
    val parser = CurlParser(tokens)

    val tree = parser.parse()

    val listener = CurlListener()
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    return listener.buildRequest()
}

fun String.parseCurl(): Request = Request.parseCurl(this)
