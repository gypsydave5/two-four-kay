package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.Method
import org.http4k.core.Request
import kotlin.test.Test
import kotlin.test.assertEquals

class RequestTest {


    @Test
    fun `generates http4k request`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com").generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
"""
        assertEquals(expected, requestString)
    }

}