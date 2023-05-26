import com.gypsydave5.twofourkay.har.HAR
import com.gypsydave5.twofourkay.har.parseHar
import org.http4k.core.*
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.body.formAsMap
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.cookies
import org.http4k.urlDecoded
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParseHarRequestTest {
    private val request = getFirstRequestFrom("motherfuckingwebsite_firefox_http_1_1.har")

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

    @Test
    fun `gets the cookies`() {
        val harRequest = getFirstRequestFrom("stackoverflow.com_chrome_http_2.har")
        val request = Request.parseHar(harRequest)

        assertEquals(
            Cookie(name = "prov", value = "ab5356ec-a671-9c2a-83eb-02a89f01d04f"),
            request.cookies().first()
        )
    }
}

class ParseHarRequestChromeTest {

    @Test
    fun `gets the url`() {
        val request = getFirstRequestFrom("html.duckduckgo.com_chrome_http_2.har")
        assertEquals(
            Uri.of(request.url),
            Request.parseHar(request).uri,
        )
    }

    @Test
    fun `gets the query string`() {
        val request = getFirstRequestFrom("www.google.com.har")

        assertEquals(
            request.queryString.map { it.name to it.value.urlDecoded() },
            Request.parseHar(request).uri.queries()
        )
    }

    @Test
    fun `can handle http_3`() {
        val request = getFirstRequestFrom("www.google.com.har")

        assertEquals(
            HttpMessage.HTTP_2,
            Request.parseHar(request).version,
        )
    }

    @Test
    fun `can read a form encoded request body`() {
        val harRequest = getFirstRequestFrom("news.ycombinator.com_firefox_http_1_1.har")
        val request = Request.parseHar(harRequest)
        assertEquals(
            mapOf(
                "goto" to listOf("news"),
                "acct" to listOf("gypsydave5"),
                "pw" to listOf("REDACTED"),
            ),
            request.formAsMap(),
        )
    }
}

fun getResourceAsText(path: String): String? = object {}.javaClass.getResource(path)?.readText()

fun getHarFromResource(path: String): HAR {
    val har = getResourceAsText(path)?.parseHar()
    assertNotNull(har)
    return har
}

private fun getFirstRequestFrom(path: String): com.gypsydave5.twofourkay.har.Request =
    getHarFromResource(path).log.entries.first().request