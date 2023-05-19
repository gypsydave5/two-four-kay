import com.gypsydave5.twofourkay.har.HAR
import com.gypsydave5.twofourkay.har.parseHar
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParseHarRequestTest {
    val request = getFirstRequestFrom("motherfuckingwebsite_firefox_http_1_1.har")

    @Test
    fun `gets the correct method`() {
        assertEquals(
            expected = Request.parseHar(request).method,
            actual = Method.GET
        )
    }

    @Test
    fun `gets the correct URL`() {
        assertEquals(
            expected = Uri.of("http://motherfuckingwebsite.com/"),
            actual = Request.parseHar(request).uri
        )
    }

    @Test
    fun `gets the correct HTTP version`() {

        assertEquals(
            expected = HTTP_1_1,
            actual = Request.parseHar(request).version
        )
    }

    @Test
    fun `gets the expected headers`() {
        assertEquals(
            listOf(
                "Host" to "motherfuckingwebsite.com",
                "User-Agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/113.0",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
                "Accept-Language" to "en-GB,en;q=0.7,en-US;q=0.3",
                "Accept-Encoding" to "gzip, deflate",
                "DNT" to "1",
                "Connection" to "keep-alive",
                "Upgrade-Insecure-Requests" to "1",
                "x-stoa-test" to "true",
                "x-stoa-incubating-features-enabled" to "true",
                "Pragma" to "no-cache",
                "Cache-Control" to "no-cache"
            ),
            Request.parseHar(request).headers
        )
    }
}

fun getResourceAsText(path: String): String? = object {}.javaClass.getResource(path)?.readText()

fun getHarFromResource(path: String): HAR {
    val h = getResourceAsText(path)?.parseHar()
    assertNotNull(h)
    return h
}

fun getFirstRequestFrom(path: String): com.gypsydave5.twofourkay.har.Request =
    getHarFromResource(path).log.entries.first().request