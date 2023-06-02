package curl

import CurlLexer
import CurlParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.http4k.core.Method
import org.http4k.core.Request
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseCurlTest {

    @Test
    fun `can parse a basic cURL command`() {
        val curl = "curl http://gypsydave5.com"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `does the right thing with double quotes`() {
        val curl = "curl \"http://gypsydave5.com\""

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `does the right thing with single quotes`() {
        val curl = "curl 'http://gypsydave5.com'"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `can parse the method`() {
        val curl = "curl --request POST http://gypsydave5.com"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.POST, "http://gypsydave5.com")

        assertEquals(expected, request)
    }
}

private fun Request.Companion.parseCurl(curl: String): Request {
    val charStream = CharStreams.fromString(curl)
    val lexer = CurlLexer(charStream)
    val commonTokenStream = CommonTokenStream(lexer)
    val parser = CurlParser(commonTokenStream)
    val tree = parser.curl()
    println(tree.uri().text)


    val method = Method.valueOf(tree?.request()?.UNQUOTED_STRING()?.text ?: "GET")

    return Request(method, tree.uri().text)
}