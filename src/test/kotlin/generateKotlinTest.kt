import com.gypsydave5.twofourkay.generateKotlin
import org.http4k.core.Method
import org.http4k.core.Request
import kotlin.test.Test
import kotlin.test.assertEquals

class GenerateKotlinTest {

    @Test
    fun `generates http4k request`() {
        val requestString = Request(Method.GET, "http://gypsydave5.com").generateKotlin()

        val expected =
            """import org.http4k.core.Method
import org.http4k.core.Request

public val request: Request = return Request(Method.GET, "http://gypsydave5.com")
"""
        assertEquals(expected, requestString)
    }
}