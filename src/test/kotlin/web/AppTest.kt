package web

import har.getResourceAsText
import io.github.gypsydave5.twofourkay.web.App
import org.http4k.core.Method
import org.http4k.core.Request

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun `can convert a HAR into a kotlin file of requests and responses`() {
        val app = App()
        val request = Request(Method.POST, "/har").body(getResourceAsText("simplest.har"))
        val response = app(request)

        val expected = transactions
        assertEquals(expected, response.bodyString())
    }

    @Test
    fun `can also convert a HAR from a GET request with a query string`() {
        val app = App()
        val request = Request(Method.GET, "/har").query("har", getResourceAsText("simplest.har"))

        val response = app(request)

        val expected = transactions
        assertEquals(expected, response.bodyString())
    }

    @Test
    fun `has some sort of index page`() {
        val app = App()
        val request = Request(Method.GET, "/")
        val response = app(request)

        assertContains(response.bodyString(), "Two Four Kay")
    }
}

val transactions = """
            |import org.http4k.core.Method
            |import org.http4k.core.Request
            |import org.http4k.core.Response
            |import org.http4k.core.Status
            |
            |/**
            | * First Transaction
            | */
            |public val firstRequest: Request = Request(Method.GET, "http://gypsydave5.com/")
            |    	.header("Host", "gypsydave5.com")
            |
            |public val firstResponse: Response = Response(Status.OK)
            |    	.header("content-type", "text/plain")
            |    	.header("content-length", "11")
            |    	.body("hello world")
            |
            |/**
            | * Second Transaction
            | */
            |public val secondRequest: Request = Request(Method.GET, "http://gypsydave5.com/")
            |    	.header("Host", "gypsydave5.com")
            |
            |public val secondResponse: Response = Response(Status.OK)
            |    	.header("content-type", "text/plain")
            |    	.header("content-length", "13")
            |    	.body("goodbye world")
            |
        """.trimMargin()