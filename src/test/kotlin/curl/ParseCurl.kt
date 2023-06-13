package curl

import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.core.toCurl
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
    fun `can parse all the methods with both long and short flags`() {
        val url = "http://gypsydave5"
        fun curlCmd(flag: String, method: Method) = "curl $flag ${method.name} $url"

        val inputs: List<Pair<String, Method>> = listOf("--request", "-X").cartesianProduct(Method.values().toList())

        inputs.forEach { (flag, method) ->
            val curl = curlCmd(flag, method)
            val request = Request.parseCurl(curl)
            val expected = Request(method, url)
            assertEquals(expected, request)
        }
    }

    @Test
    fun `can parse the headers short flag`() {
        val curl = "curl -H 'Connection: keep-alive' http://gypsydave5.com"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `is cool with the options appearing after the url`() {
        val curl = "curl http://gypsydave5.com -H 'Connection: keep-alive'"

        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "http://gypsydave5.com")
            .header("Connection", "keep-alive")

        assertEquals(expected, request)
    }

    @Test
    fun `handles header values with colons in gracefully`() {
        val curl = "curl 'https://blog.gypsydave5.com/' -H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'"
        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "https://blog.gypsydave5.com/")
            .header("If-Modified-Since", "Wed, 10 Aug 2022 09:16:26 GMT")

        assertEquals(expected, request)
    }

    @Test
    fun `handles new line backslashes gracefully`() {
        val curl = """curl 'https://blog.gypsydave5.com/' \
            |-H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'""".trimMargin()
        val request = Request.parseCurl(curl)

        val expected = Request(Method.GET, "https://blog.gypsydave5.com/")
            .header("If-Modified-Since", "Wed, 10 Aug 2022 09:16:26 GMT")

        assertEquals(expected, request)
    }

    @Test
    fun `can do a post`() {
        val curl = exampleCurlFromChrome
        val request = Request.parseCurl(curl)

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
    fun `extensive examples`() {
        (0..20).forEach {
            val request: Request = Request.random()
            println(request)
            assertEquals(request, Request.parseCurl(request.toCurl()))
        }
    }
}

fun Request.Companion.random(): Request = Request(Method.random(), Uri.random())

private fun Uri.Companion.random(): Uri = of("http://${String.random()}.com")

private fun Method.Companion.random(): Method = Method.values().random()

private fun String.Companion.random(
    size: Int = 20,
    chars: Iterable<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
): String =
    CharArray(size) { chars.toList().random() }.concatToString()


private val exampleCurlFromFirefox = """curl 'https://blog.gypsydave5.com/' 
    |-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/113.0' 
    |-H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8' 
    |-H 'Accept-Language: en-GB,en;q=0.7,en-US;q=0.3' 
    |-H 'Accept-Encoding: gzip, deflate, br' 
    |-H 'DNT: 1' 
    |-H 'Connection: keep-alive' 
    |-H 'Upgrade-Insecure-Requests: 1' 
    |-H 'Sec-Fetch-Dest: document' 
    |-H 'Sec-Fetch-Mode: navigate' 
    |-H 'Sec-Fetch-Site: none' 
    |-H 'Sec-Fetch-User: ?1' 
    |-H 'If-Modified-Since: Wed, 10 Aug 2022 09:16:26 GMT'
    |-H 'If-None-Match: "7d51b9981a61c7f2fe01949a0dd5c20b"'"""
    .trimMargin()

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

private val exampleCurlPostFromChrome = """curl 'http://google.com/' \
                    |-X 'POST' \
                    |-H 'Content-Type: application/x-www-form-urlencoded' \
                    |-H 'Origin: null' \
                    |-H 'Upgrade-Insecure-Requests: 1' \
                    |-H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36' \
                    |--compressed""".trimMargin()


private fun <S, T> Iterable<S>.cartesianProduct(other: Iterable<T>) =
    this.flatMap { thisIt -> other.map { otherIt -> thisIt to otherIt } }