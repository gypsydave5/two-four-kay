package web

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.web.CurlHandler
import org.http4k.appendIf
import org.http4k.appendIfNotBlank
import org.http4k.appendIfNotEmpty
import org.http4k.core.HttpMessage.Companion.HTTP_2
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.quoted
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

fun Request.toCurl(): String =
    StringBuilder("curl")
        .append(" -X $method")
        .appendIf({ version == HTTP_2 }, " --http2")
        .appendIfNotEmpty(
            headers,
            " " + headers.joinToString(" ") { """-H ${(it.first + ":" + it.second).quoted()}""" })
        .appendIfNotBlank(body.toString(), " --data ${body.toString().quoted()}")
        .append(" \"$uri\"")
        .toString()