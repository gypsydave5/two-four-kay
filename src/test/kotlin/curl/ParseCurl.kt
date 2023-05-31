package curl

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
}

private fun Request.Companion.parseCurl(curl: String): Request {
    var tokens = curl.split(" ")
    if (tokens.first() == "curl") {
        tokens = tokens.drop(1)
    }

    return Request(Method.GET, tokens.last())
}
