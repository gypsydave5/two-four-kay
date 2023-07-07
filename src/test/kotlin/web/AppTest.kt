package web

import Configuration
import har.getResourceAsText
import io.github.gypsydave5.twofourkay.web.App
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun `has some sort of index page`() {
        val app = App(TestConfig)
        val request = Request(Method.GET, "/")
        val response = app(request)

        assertContains(response.bodyString(), "Two Four Kay")
    }

    @Test
    fun `you can use the index page to submit a form with a HAR in it`() {
        val app = App(TestConfig)

        val driver = Http4kWebDriver(app)

        driver.navigate().to("/")
        driver.findElement(By.cssSelector("textarea"))!!.sendKeys(getResourceAsText("simplest.har"))
        driver.findElement(By.cssSelector("form"))!!.submit()
        assertEquals(Status.OK, driver.status)

        assertEquals(transactions, driver.findElement(By.cssSelector("#output"))!!.text)
    }

    @Test
    fun `you can use the index page to submit a form with a curl command in it`() {
        val app = App(TestConfig)

        val driver = Http4kWebDriver(app)

        driver.navigate().to("/")
        driver.findElement(By.cssSelector("textarea"))!!.sendKeys("curl -X POST https://http4k.org")
        driver.findElement(By.cssSelector("form"))!!.submit()

        assertEquals(Status.OK, driver.status)
        assertEquals(
            """import org.http4k.core.Method
              |import org.http4k.core.Request

              |public val request: Request = Request(Method.POST, "https://http4k.org")""".trimMargin(),
            driver.findElement(By.cssSelector("#output"))!!.text
        )
    }

    @Test
    fun `you can use the index page to submit a form with a wire format HTTP request in it`() {
        val app = App(TestConfig)

        val driver = Http4kWebDriver(app)

        driver.navigate().to("/")
        driver.findElement(By.cssSelector("textarea"))!!.sendKeys("TRACE https://http4k.org HTTP/1.1")
        driver.findElement(By.cssSelector("form"))!!.submit()

        assertEquals(Status.OK, driver.status)
        assertEquals(
            """import org.http4k.core.Method
              |import org.http4k.core.Request

              |public val request: Request = Request(Method.TRACE, "https://http4k.org")""".trimMargin(),
            driver.findElement(By.cssSelector("#output"))!!.text
        )
    }

    @Test
    fun `you can use the index page to submit a form with a wire format HTTP response in it`() {
        val app = App(TestConfig)

        val driver = Http4kWebDriver(app)

        driver.navigate().to("/")
        driver.findElement(By.cssSelector("textarea"))!!.sendKeys("HTTP/2 9 WHATEVS")
        driver.findElement(By.cssSelector("form"))!!.submit()

        assertEquals(Status.OK, driver.status)
        assertEquals(
            """import org.http4k.core.HttpMessage
              |import org.http4k.core.Response
              |import org.http4k.core.Status

              |public val response: Response = Response(Status(9, "WHATEVS"), HttpMessage.HTTP_2)""".trimMargin(),
            driver.findElement(By.cssSelector("#output"))!!.text
        )
    }
}

object TestConfig : Configuration {
    override val server_port: Int = 8080
    override val env: String = "test"
}


