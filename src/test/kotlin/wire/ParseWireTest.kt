package wire

import curl.random
import dev.forkhandles.result4k.orThrow
import io.github.gypsydave5.twofourkay.parse.wire.parseWire
import org.http4k.core.Request
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseWireTest {
    @Test
    fun `a request, when converted toCurl and then parsed back from cURL, is the identical`() {
        repeat(2000) {
            val request: Request = Request.random()
            val parsed = Request.parseWire(request.toMessage()).orThrow()
            assertEquals(request, parsed)
        }
    }
}