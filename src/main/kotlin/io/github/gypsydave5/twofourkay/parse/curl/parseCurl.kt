package io.github.gypsydave5.twofourkay.parse.curl

import CurlLexer
import CurlParser
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.http4k.core.Request

fun Request.Companion.parseCurl(curl: String): Result<Request, Error> {
    val errorListener = ErrorListener()

    val input = CharStreams.fromString(curl)
    val lexer = CurlLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    val tokens = CommonTokenStream(lexer)
    val parser = CurlParser(tokens)

    errorListener.error?.let { return Failure(it) }

    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)
    val tree = parser.parse()

    errorListener.error?.let { return Failure(it) }


    val listener = CurlListener()
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    return Success(listener.buildRequest())
}

fun String.parseCurl(): Result<Request, Error> = Request.parseCurl(this)
