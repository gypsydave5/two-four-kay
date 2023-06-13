package har

import dev.forkhandles.result4k.failureOrNull
import io.github.gypsydave5.twofourkay.parse.har.ParseHarError
import io.github.gypsydave5.twofourkay.parse.har.parseHar
import kotlin.test.Test

import kotlin.test.assertIs

class ParseHarTest {

    @Test
    fun `blows up gracefully when it can't parse a HAR`() {
        val notHar = "not har"

        val result = notHar.parseHar()

        assertIs<ParseHarError>(result.failureOrNull())
    }
}