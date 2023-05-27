import io.github.gypsydave5.twofourkay.har.toHttpTransactions
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseHarCollectionTest {

    @Test
    fun `parses a whole HAR as a collection of requests and responses`() {
        val har = getHarFromResource("all_http_methods_firefox_http_1_1.har")

        val communications = har.toHttpTransactions()

        assertEquals(7, communications.size)

        val methods = communications.map { communication -> communication.request.method.toString() }

        methods.containsAll(listOf("POST", "PATCH", "PUT", "DELETE", "OPTIONS", "GET", "HEAD"))
    }
}