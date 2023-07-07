package web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.web.CurlHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.toCurl
import random.random
import kotlin.test.Test
import kotlin.test.assertEquals

class CurlHandlerTest {
    @Test
    fun `can turn a curl into Kotlin`() {
        val expected = Request.random()
        val curl = expected.toCurl()
        val handler = CurlHandler()

        val request = Request(Method.POST, "/").body(curl)
        val response = handler(request)

        assertEquals(expected.generateKotlin(), response.bodyString())
    }
}