import io.github.gypsydave5.twofourkay.har.parseHar
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.SameSite
import org.http4k.core.cookie.cookies
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseHarResponseTest {
    private val res: io.github.gypsydave5.twofourkay.har.Response =
        getFirstResponseFrom("motherfuckingwebsite_firefox_http_1_1.har")

    @Test
    fun `can get the HTTP version number`() {
        assertEquals(
            expected = HTTP_1_1,
            actual = Response.parseHar(res).version,
        )
    }

    @Test
    fun `gets the status code`() {
        assertEquals(
            expected = Status(res.status.toInt(), res.statusText),
            actual = Response.parseHar(res).status,
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
            actual = Response.parseHar(res).headers
        )
    }

    @Test
    fun `can get the body`() {
        assertEquals(
            expected = res.content.text,
            actual = Response.parseHar(res).bodyString()
        )
    }
}

class ParseHarResponseChromeTest {

    @Test
    fun `gets the cookies`() {
        val res = getFirstResponseFrom("www.google.com.har")

        assertEquals(
            Cookie(
                name = "__Secure-ENID",
                value = "12.SE=nY_FLW-ayqoVZ0vBp2Lx0RWTV08vQpcPkAEaORmmR4Plai7Qvhl7t773zYcMjxVwpwed7JXXy67zZW42q5nJMrBbNs2xKApaNz9aCgBpZ9xnFhmVabwxs7_35tUuzRpfcc53Ib7ajPUVuX1UpEWwFbR9dNC6k3nrRz1MFMXkbEd7mq1JD9DTEvXmxXMOBksJ8ZcN0CFV7yVi5B2VdmiJgsNr8tfA3nayA9u0hgx-8h6D01uQgREgHkCh8zYiMkddnwLH7tDhe9hlVZYj6tc4qyAB-Q",
                path = "/",
                domain = ".google.com",
                expires = Instant.parse("2024-05-24T06:00:44.000Z"),
                httpOnly = true,
                secure = true,
                sameSite = SameSite.Lax
            ),
            Response.parseHar(res).cookies().first()
        )
    }
}

private fun getFirstResponseFrom(path: String): io.github.gypsydave5.twofourkay.har.Response =
    getHarFromResource(path).log.entries.first().response
