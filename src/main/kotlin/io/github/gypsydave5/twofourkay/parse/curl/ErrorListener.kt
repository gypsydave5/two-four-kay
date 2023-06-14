package io.github.gypsydave5.twofourkay.parse.curl

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

internal class ErrorListener : BaseErrorListener() {
    var error: CurlParseError? = null

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        error = CurlParseError("line $line:$charPositionInLine $msg")
    }
}

class CurlParseError(message: String) : Error(message)