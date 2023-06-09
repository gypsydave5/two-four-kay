package web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.web.curlHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.toCurl
import kotlin.test.Test
import kotlin.test.assertEquals

class CurlHandlerTest {

    @Test
    fun `can turn a curl into Kotlin`() {
        val requestToCurl = Request(Method.POST, "/").body("hello")
        val curl = requestToCurl.toCurl()
        val request = Request(Method.POST, "/").body(curl)
        val response = curlHandler(request)

        assertEquals(requestToCurl.generateKotlin(), response.bodyString())
    }
}