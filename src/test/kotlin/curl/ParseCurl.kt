package curl

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import java.util.*
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
        val curl = "curl -X POST 'http://gypsydave5.com'"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.POST, "http://gypsydave5.com")

        assertEquals(expected, request)
    }
}

private fun Request.Companion.parseCurl(curl: String): Request {
    val input = Scanner(curl).useDelimiter(" ")
    var request = Request(Method.GET, "")

    if (input.hasNext("curl")) {
        input.next()
    }

    while (input.hasNext()) {
        if (input.hasNext("-.*")) {
            request = parseFlag(input, request)
        }

        request = parseUri(input, request)
    }



    return request
}

private fun String.removeSurroundingQuotes() = trim()
    .removeSurrounding("\"")
    .removeSurrounding("'")

fun parseFlag(input: Scanner, request: Request): Request {
    println(">>>>>" + input.next())
    return request.method(Method.valueOf(input.next().removeSurroundingQuotes()))
}

fun parseUri(input: Scanner, request: Request): Request {
    return request.uri(Uri.of(input.next().removeSurroundingQuotes()))
}
