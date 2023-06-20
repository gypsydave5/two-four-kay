package web

import har.getResourceAsText
import io.github.gypsydave5.twofourkay.web.harHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class HarHandlerTest {
    @Test
    fun `can convert a HAR into a kotlin file of requests and responses`() {
        val request = Request(Method.POST, "").body(getResourceAsText("simplest.har"))
        val response = harHandler(request)

        assertEquals(transactions, response.bodyString().trim())
    }

    @Test
    fun `tells you you're doing it wrong if you aren't POSTing a HAR`() {
        val request = Request(Method.POST, "").body("not a har")
        val response = harHandler(request)

        assertEquals(Status.BAD_REQUEST, response.status)
        assertContains(response.bodyString(), "Unable to parse HAR")
    }

    @Test
    fun `can also convert a HAR from a GET request with a query string`() {
        val request = Request(Method.GET, "").query("har", getResourceAsText("simplest.har"))

        val response = harHandler(request)

        assertEquals(Status.OK, response.status)
        assertEquals(transactions, response.bodyString().trim())
    }

    @Test
    fun `tells you you're doing it wrong if you're not sending a HAR`() {
        val request = Request(Method.GET, "").query("har", "not a har")

        val response = harHandler(request)

        assertEquals(Status.BAD_REQUEST, response.status)
        assertContains(response.bodyString(), "Unable to parse HAR")
    }

    @Test
    fun `tells you you're doing it wrong if you've not got a har parameter`() {
        val request = Request(Method.GET, "")

        val response = harHandler(request)

        assertEquals(Status.BAD_REQUEST, response.status)
        assertContains(response.bodyString(), "Missing har parameter in query string")
    }
}