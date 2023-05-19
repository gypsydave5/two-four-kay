import com.gypsydave5.twofourkay.generateKotlin
import org.http4k.core.Method
import org.http4k.core.Request
import kotlin.test.Test

class GenerateKotlinTest {

    @Test
    fun `generates http4k request`() {
        val request = Request(Method.GET, "http://gypsydave5.com").generateKotlin()
        println(request.toString())
    }
}