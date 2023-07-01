package wire


import HttpBaseListener
import HttpLexer
import HttpParser
import curl.random
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow
import io.github.gypsydave5.twofourkay.parse.curl.ErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.http4k.core.HttpMessage
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@Ignore
class ParseWireTest {
    @Test
    fun `a request, when converted toCurl and then parsed back from cURL, is the identical`() {
        repeat(200) {
            val request: Request = Request.random()
            println(request.toMessage())
            assertEquals(request, Request.parseWire(request.toMessage()).orThrow())
        }
    }

    @Test
    fun `bad apples`() {
        repeat(200) {
            assertEquals(Request.random(), Request.parseWire(badOne).orThrow())
        }
    }
}

private fun Request.Companion.parseWire(wire: String): Result<HttpMessage, Error> {
    val errorListener = ErrorListener()

    val input = CharStreams.fromString(wire)
    val lexer = HttpLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errorListener)

    val tokens = CommonTokenStream(lexer)
    val parser = HttpParser(tokens)

    errorListener.error?.let { return Failure(it) }

    parser.removeErrorListeners()
    parser.addErrorListener(errorListener)

    val tree = parser.http_message()

    val listener = HttpListener()
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    return Success(listener.buildRequest())
}

private class HttpListener : HttpBaseListener() {
    private var request: Request = Request(Method.GET, "")

    override fun enterMethod(ctx: HttpParser.MethodContext) {
        request = request.method(Method.valueOf(ctx.text))
        super.enterMethod(ctx)
    }

    override fun enterRequest_target(ctx: HttpParser.Request_targetContext) {
        request = request.uri(Uri.of(ctx.text))
    }

    override fun enterBody(ctx: HttpParser.BodyContext) {
        request = request.body(ctx.text.trimStart('\n', '\r'))
    }

    override fun enterHeader_field(ctx: HttpParser.Header_fieldContext) {
        request = request.header(ctx.field_name().text, ctx.field_value().text)
    }

    fun buildRequest(): Request = request
}

val badOne = """DELETE ?igjt21Jki5XjZCAGxt1x#rzuiMkZapaCotxxsmymf HTTP/1.1
nvCvfiukoN3Cvq0T2Nu6: B5UsoRkh32GCLbNEzv5i
JqF89loEjbIYwVBWpz75: DvJZk7d1dqblrEC6Y8Sz
ubTScL5cAgLEoT4caNYl: 67ZNa7SGpLBPYbn4PCvN
GET3qNVotop6yrBBLGMB: GETDboxjd8CXWet7vLeg
NC3g43NmcB99Fu3tp3AM: Nb0BG3rDfEmtCc13yFWg
8TXszfoT2wQZqaMeB0vS: czN06UrumrQDvrV0AJ9B

DWYTErlkA9IjN8Us1lcZ"""
