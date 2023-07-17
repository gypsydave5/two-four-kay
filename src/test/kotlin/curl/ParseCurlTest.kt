package curl

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.orThrow
import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import org.http4k.core.HttpMessage.Companion.HTTP_2
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import random.random
import random.randomly
import web.toCurl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParseCurlTest {

    @Test
    fun `can parse a basic cURL command`() {
        val curl = "curl http://gypsydave5.com"

        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `does the right thing with double quotes`() {
        val curl = "curl \"http://gypsydave5.com\""

        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `does the right thing with single quotes`() {
        val curl = "curl 'http://gypsydave5.com'"

        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "http://gypsydave5.com")

        assertEquals(expected, request)
    }

    @Test
    fun `can parse all the methods with both long and short flags`() {
        val url = "http://gypsydave5"
        fun curlCmd(flag: String, method: Method) = "curl $flag ${method.name} $url"

        val inputs: List<Pair<String, Method>> = listOf("--request", "-X").cartesianProduct(Method.values().toList())

        inputs.forEach { (flag, method) ->
            val curl = curlCmd(flag, method)
            val request = Request.parseCurl(curl).orThrow()
            val expected = Request(method, url)
            assertEquals(expected, request)
        }
    }

    @Test
    fun `can parse the headers short flag`() {
        val curl = "curl -H 'Connection: keep-alive' http://gypsydave5.com"

        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `is cool with the options appearing after the url`() {
        val curl = "curl http://gypsydave5.com -H 'Connection: keep-alive'"

        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `handles header values with colons in gracefully`() {
        val curl = "curl 'https://blog.gypsydave5.com/' -H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'"
        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "https://blog.gypsydave5.com/")
            .header("If-Modified-Since", "Wed, 10 Aug 2022 09:16:26 GMT")

        assertEquals(expected, request)
    }

    @Test
    fun `handles new line backslashes gracefully`() {
        val curl = """curl 'https://blog.gypsydave5.com/' \
            |-H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'""".trimMargin()
        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "https://blog.gypsydave5.com/")
            .header("If-Modified-Since", "Wed, 10 Aug 2022 09:16:26 GMT")

        assertEquals(expected, request)
    }

    @Test
    fun `can do a post`() {
        val curl = exampleCurlFromChrome
        val request = Request.parseCurl(curl).orThrow()

        val expected = Request(Method.GET, "https://html.duckduckgo.com/html/")
            .header("authority", "html.duckduckgo.com")
            .header(
                "accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"
            )
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("cache-control", "max-age=0")
            .header("content-type", "application/x-www-form-urlencoded")
            .header("origin", "https://html.duckduckgo.com")
            .header("referer", "https://html.duckduckgo.com/")
            .header("sec-ch-ua", """"Google Chrome";v="113", "Chromium";v="113", "Not-A.Brand";v="24"""")
            .header("sec-ch-ua-mobile", "?0")
            .header("sec-ch-ua-platform", """"macOS"""")
            .header("sec-fetch-dest", "document")
            .header("sec-fetch-mode", "navigate")
            .header("sec-fetch-site", "same-origin")
            .header("sec-fetch-user", "?1")
            .header("upgrade-insecure-requests", "1")
            .header(
                "user-agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
            )
            .body("q=http4k&b=")

        assertEquals(expected, request)
    }

    @Test
    fun `can do HTTP version 2`() {
        randomly {
            val request = Request(method = Method.random(), Uri.random(), HTTP_2)
            val actual = Request.parseCurl(request.toCurl()).orThrow()
            assertEquals(request.version, actual.version)
        }
    }

    @Test
    fun `a request, when converted toCurl and then parsed back from cURL, is the identical`() {
        randomly {
            repeat(20) {
                println(Int.random(1, 100))
                val request: Request = Request.random()
                assertEquals(request, Request.parseCurl(request.toCurl()).orThrow())
            }
        }
    }

    @Test
    fun `gracefully handles bad output`() {
        val badCommand = "this is not a curl"
        val result = Request.parseCurl(badCommand)
        assertTrue(result is Failure)
    }
}

private val exampleCurlFromChrome = """curl 'https://html.duckduckgo.com/html/' \
        |-H 'authority: html.duckduckgo.com' \
        |-H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7' \
        |-H 'accept-language: en-GB,en-US;q=0.9,en;q=0.8' \
        |-H 'cache-control: max-age=0' \
        |-H 'content-type: application/x-www-form-urlencoded' \
        |-H 'origin: https://html.duckduckgo.com' \
        |-H 'referer: https://html.duckduckgo.com/' \
        |-H 'sec-ch-ua: "Google Chrome";v="113", "Chromium";v="113", "Not-A.Brand";v="24"' \
        |-H 'sec-ch-ua-mobile: ?0' \
        |-H 'sec-ch-ua-platform: "macOS"' \
        |-H 'sec-fetch-dest: document' \
        |-H 'sec-fetch-mode: navigate' \
        |-H 'sec-fetch-site: same-origin' \
        |-H 'sec-fetch-user: ?1' \
        |-H 'upgrade-insecure-requests: 1' \
        |-H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36' \
        |--data-raw 'q=http4k&b=' \
        |--compressed""".trimMargin()

private fun <S, T> Iterable<S>.cartesianProduct(other: Iterable<T>): List<Pair<S, T>> =
    this.flatMap { thisIt -> other.map { otherIt -> thisIt to otherIt } }