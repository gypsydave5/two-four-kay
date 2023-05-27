import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.har.Communications
import io.github.gypsydave5.twofourkay.har.generateKotlin
import org.http4k.core.HttpMessage.Companion.HTTP_1_1
import org.http4k.core.HttpMessage.Companion.HTTP_2
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.body.form
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
        val commsString = Communications(
            listOf(
                Request(Method.GET, "http://gypsydave5.com") to Response(Status.OK),
                Request(Method.GET, "http://lisastansfield.com/my-baby", HTTP_1_1) to Response(Status.NOT_FOUND),
                Request(Method.GET, "http://oldgrey.com", HTTP_2) to Response(Status.HTTP_VERSION_NOT_SUPPORTED),
                Request(Method.POST, "http://andrew.wk?q=when+its+time+to+party")
                        to Response(Status.OK).body("PARTY HARD"),
                Request(Method.PATCH, "http://kurious.oranj").form("name", "priest").form("size", "big")
                        to Response(Status.OK).body("DRINK THE LONG DRAUGHT DOWN"),
            )
        ).generateKotlin().trimIndent()

        val expected = """import org.http4k.core.HttpMessage
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

public val request0: Request = Request(Method.GET, "http://gypsydave5.com")

public val response0: Response = Response(Status.OK)

public val request1: Request = Request(Method.GET, "http://lisastansfield.com/my-baby")

public val response1: Response = Response(Status.NOT_FOUND)

public val request2: Request = Request(Method.GET, "http://oldgrey.com", HttpMessage.HTTP_2)

public val response2: Response = Response(Status.HTTP_VERSION_NOT_SUPPORTED)

public val request3: Request = Request(Method.POST, "http://andrew.wk")
    	.query(""${'"'}q""${'"'}, ""${'"'}when its time to party""${'"'})

public val response3: Response = Response(Status.OK)
    	.body(""${'"'}PARTY HARD""${'"'})

public val request4: Request = Request(Method.PATCH, "http://kurious.oranj")
    	.body(""${'"'}name=priest&size=big""${'"'})

public val response4: Response = Response(Status.OK)
    	.body(""${'"'}DRINK THE LONG DRAUGHT DOWN""${'"'})""".trimIndent()

        assertEquals(expected, commsString)
    }
}
