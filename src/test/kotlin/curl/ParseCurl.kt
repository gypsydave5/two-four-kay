package curl

import CurlBaseListener
import CurlLexer
import CurlParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
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

    @Test
    fun `can parse the method short flag`() {
        val curl = "curl -X POST http://gypsydave5.com"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.POST, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `can parse the headers short flag`() {
        val curl = "curl -H 'Connection: keep-alive' http://gypsydave5.com"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `is cool with the options appearing after the url`() {
        val curl = "curl http://gypsydave5.com -H 'Connection: keep-alive'"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `handles header values with colons in gracefully`() {
        val curl = "curl 'https://blog.gypsydave5.com/' -H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'"
        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "https://blog.gypsydave5.com/")
            .header("If-Modified-Since", "Wed, 10 Aug 2022 09:16:26 GMT")

        assertEquals(expected, request)
    }
}

private fun Request.Companion.parseCurl(curl: String): Request {
    val input = CharStreams.fromString(curl)
    val lexer = CurlLexer(input)
    val tokens = CommonTokenStream(lexer)
    val parser = CurlParser(tokens)

    val tree = parser.parse()

    val listener = CurlListener()
    ParseTreeWalker.DEFAULT.walk(listener, tree)

    return listener.buildRequest()
}


class CurlListener : CurlBaseListener() {
    private var request: Request = Request(Method.GET, "")

    override fun enterUrl(ctx: CurlParser.UrlContext) {
        request = request.uri(Uri.of(ctx.text.stripQuotes()))
    }

    override fun enterOption(ctx: CurlParser.OptionContext) {
        val optionName = ctx.optionName().text
        val optionValue = ctx.optionValue()?.text?.stripQuotes()

        request = when (optionName) {
            "-X", "--request" -> optionValue?.let { request.method(Method.valueOf(it)) }
            "-H", "--header" -> {
                optionValue
                    ?.split(':', ignoreCase = false, limit = 2)
                    ?.let { request.header(it.first().trim(), it[1].trim()) }
            }

            "d", "data" -> optionValue?.let { request.body(it) }
            else -> null
        } ?: request
    }

    fun buildRequest(): Request {
        return request
    }
}

private fun String.stripQuotes(): String = when (first()) {
    '\'' -> removeSurrounding("\'")
    '\"' -> removeSurrounding("\"")
    else -> this
}

private val exampleCurlFromFirefox = """curl 'https://blog.gypsydave5.com/' 
    |-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/113.0' 
    |-H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8' 
    |-H 'Accept-Language: en-GB,en;q=0.7,en-US;q=0.3' 
    |-H 'Accept-Encoding: gzip, deflate, br' 
    |-H 'DNT: 1' 
    |-H 'Connection: keep-alive' 
    |-H 'Upgrade-Insecure-Requests: 1' 
    |-H 'Sec-Fetch-Dest: document' 
    |-H 'Sec-Fetch-Mode: navigate' 
    |-H 'Sec-Fetch-Site: none' 
    |-H 'Sec-Fetch-User: ?1' 
    |-H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT' 
    |-H 'If-None-Match: "7d51b9981a61c7f2fe01949a0dd5c20b"'"""
    .trimMargin()