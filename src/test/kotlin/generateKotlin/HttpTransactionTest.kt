package generateKotlin

import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.*
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.test.assertEquals

class HttpTransactionTest {

    @Test
    fun `generates a series of http4k requests and responses`() {
        val commsString =
            listOf(
                Request(Method.GET, "http://gypsydave5.com") to Response(Status.OK),
                Request(Method.GET, "http://lisastansfield.com/my-baby") to Response(Status.NOT_FOUND),
            )
                .map(HttpTransaction.Companion::fromPair)
                .generateKotlin().trimIndent()

        val expected = """import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

/**
 * First Transaction
 */
public val firstRequest: Request = Request(Method.GET, "http://gypsydave5.com")

public val firstResponse: Response = Response(Status.OK)

/**
 * Second Transaction
 */
public val secondRequest: Request = Request(Method.GET, "http://lisastansfield.com/my-baby")

public val secondResponse: Response = Response(Status.NOT_FOUND)""".trimIndent()

        assertEquals(expected, commsString)
    }

}

private fun HttpTransaction.Companion.fromPair(pair: Pair<Request, Response>): HttpTransaction =
    HttpTransaction(pair.first, pair.second, Duration.ZERO)