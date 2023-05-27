package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
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
    fun `when the status is not conventional, generate it in full`() {
        val responseString = Response(Status(431, "Request Header Fields Too Large")).generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status(431, "Request Header Fields Too Large"))
"""
        assertEquals(expected, responseString)
    }
}