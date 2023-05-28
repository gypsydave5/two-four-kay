package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.HttpMessage
import org.http4k.core.Response
import org.http4k.core.Status
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTest {

    @Test
    fun `generates http4k response`() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE).generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun bodies() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
            .body("body")
            .generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
    	.body("body")
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun `big bodies`() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
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
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
    	.body(""${'"'}
    |this
    |is
    |a
    |long
    |body
    ""${'"'}.trimMargin())
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun headers() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
            .header("do", "\$what")
            .header("eh?", null)
            .generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
    	.header("do", "${'$'}{'${'$'}'}what")
    	.header("eh?", null)
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun `when the status is not conventional, generate it in full`() {
        val responseString = Response(Status(431, "Request Header Fields Too Large")).generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status(431, "Request Header Fields Too Large"))
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun `explicitly show the HTTP version when it is not 1_1`() {
        val responseString = Response(Status.OK, HttpMessage.HTTP_2).generateKotlin()

        val expected =
            """import org.http4k.core.HttpMessage
import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.OK, HttpMessage.HTTP_2)
"""
        assertEquals(expected, responseString)
    }
}