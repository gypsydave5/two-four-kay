package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.HttpMessage
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

    @Test
    fun bodies() {
        val requestString = Request(Method.GET, "http://gypsydave5.com")
            .body("body")
            .generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
    	.body("body")
"""
        assertEquals(expected, requestString)
    }

    @Test
    fun `big bodies`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com")
            .body(
                """this
                |is
                |a
                |long
                |body
            """.trimMargin()
            )
            .generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
    	.body(""${'"'}
    |this
    |is
    |a
    |long
    |body
    ""${'"'}.trimMargin())
"""
        assertEquals(expected, requestString)
    }

    @Test
    fun `escapes the dollar in bodies with dollars`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com")
            .body("${'$'}body")
            .generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
    	.body("${'$'}{'$'}body")
"""
        assertEquals(expected, requestString)
    }

    @Test
    fun `headers with dollars`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com")
            .header("do", "\$what")
            .header("eh?", null)
            .generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
    	.header("do", "${'$'}{'${'$'}'}what")
    	.header("eh?", null)
"""
        assertEquals(expected, requestString)
    }

    @Test
    fun `query string (with dollars)`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com")
            .query("do", "\$what")
            .query("eh?", null)
            .generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com")
    	.query("do", "${'$'}{'${'$'}'}what")
    	.query("eh?", null)
"""
        assertEquals(expected, requestString)
    }

    @Test
    fun `explicitly show the HTTP version when it is not 1_1`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com", HttpMessage.HTTP_2).generateKotlin()

        val expected =
            """import org.http4k.core.HttpMessage
import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = Request(Method.GET, "http://gypsydave5.com", HttpMessage.HTTP_2)
"""
        assertEquals(expected, requestString)
    }
}
