package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.Response
import org.http4k.core.Status
import kotlin.test.Test
import kotlin.test.assertEquals

class ResponseTest {
    
    @Test
    fun `generates http4k response`() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE, "http://gypsydave5.com").generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
"""
        assertEquals(expected, responseString)
    }
}