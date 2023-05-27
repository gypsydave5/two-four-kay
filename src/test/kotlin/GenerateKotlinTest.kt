import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import org.http4k.core.*
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals

class GenerateKotlinTest {

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
    fun `generates http4k response`() {
        val responseString = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE, "http://gypsydave5.com").generateKotlin()

        val expected =
            """import org.http4k.core.Response
import org.http4k.core.Status

public val response: Response = Response(Status.REQUESTED_RANGE_NOT_SATISFIABLE)
"""
        assertEquals(expected, responseString)
    }

    @Test
    fun `generates a series of http4k requests and responses`() {
        val commsString =
            listOf(
                Request(Method.GET, "http://gypsydave5.com") to Response(Status.OK),
                Request(Method.GET, "http://lisastansfield.com/my-baby") to Response(Status.NOT_FOUND),
//                Request(Method.GET, "http://oldgrey.com", HTTP_2) to Response(Status.HTTP_VERSION_NOT_SUPPORTED),
//                Request(Method.POST, "http://andrew.wk?q=when+its+time+to+party")
//                        to Response(Status.OK).body("PARTY HARD"),
//                Request(Method.PATCH, "http://kurious.oranj").form("name", "priest").form("size", "big")
//                        to Response(Status.OK).body("DRINK THE LONG DRAUGHT DOWN"),
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
