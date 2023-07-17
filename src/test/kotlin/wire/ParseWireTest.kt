package wire

import dev.forkhandles.result4k.failureOrNull
import dev.forkhandles.result4k.orThrow
import io.github.gypsydave5.twofourkay.parse.wire.InvalidStatusCode
import io.github.gypsydave5.twofourkay.parse.wire.parseWire
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import random.random
import random.randomly
import random.repeatRandomly
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ParseWireTest {
    @Test
    fun `a request, when converted toCurl and then parsed back from cURL, is the identical`() {
        repeat(2000) {
            val request: Request = randomly { Request.random() }
            val parsed = Request.parseWire(request.toMessage()).orThrow()
            assertEquals(request, parsed)
        }
    }

    @Test
    fun `a response, when converted toCurl and then parsed back from cURL, is the identical`() {
        repeatRandomly(2000) {
            val response: Response = Response.random()
            val parsed = Response.parseWire(response.toMessage()).orThrow()
            assertEquals(response, parsed)
        }
    }

    @Test
    fun `fails nicely when the status code isn't an integer`() {
        val statusCode = randomly { String.random() }
        val error = Response.parseWire("HTTP/1.1 $statusCode bobbitybob").failureOrNull()
        assertNotNull(error)
        assertEquals(error, InvalidStatusCode(statusCode))
    }

    @Test
    fun `but will put up status codes outside of the bounds of the RFC`() {
        repeat(2000) {
            val statusCode = randomly { Int.random(-10000, 10000) }
            val statusString = "bobbitybob"
            val actual = Response.parseWire("HTTP/1.1 $statusCode $statusString").orThrow()

            val expected = Response(Status(statusCode, statusString), HTTP_1_1)
            assertEquals(expected, actual)
        }
    }
}

