import com.gypsydave5.twofourkay.har.Response
import com.gypsydave5.twofourkay.har.parseHar
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseHarResponseTest {
    val res = getFirstResponseFrom("motherfuckingwebsite_firefox_http_1_1.har")

    @Test
    fun `can get the HTTP version number`() {
        assertEquals(
            expected = HTTP_1_1,
            actual = res.httpVersion,
        )
    }

    @Test
    fun `can get the headers`() {
        assertEquals(
            expected = listOf(
                "Connection" to "Keep-Alive",
                "Keep-Alive" to "timeout=5, max=100",
                "content-type" to "text/html",
                "last-modified" to "Sun, 18 Jan 2015 00:04:33 GMT",
                "accept-ranges" to "bytes",
                "content-encoding" to "gzip",
                "vary" to "Accept-Encoding",
                "content-length" to "2290",
                "date" to "Fri, 19 May 2023 12:04:51 GMT",
                "server" to "LiteSpeed",
            ),
            actual = org.http4k.core.Response.parseHar(res).headers
        )
    }

    @Test
    fun `can get the body`() {
        assertEquals(
            expected = res.content.text,
            actual = org.http4k.core.Response.parseHar(res).bodyString()
        )
    }
}

private fun getFirstResponseFrom(path: String): Response = getHarFromResource(path).log.entries.first().response
