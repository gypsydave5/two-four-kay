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
        val app = App(Configuration())
        val request = Request(Method.GET, "/")
        val response = app(request)

        assertContains(response.bodyString(), "Two Four Kay")
    }

    @Test
    fun `you can use the index page to submit a form with a HAR in it`() {
        val app = App(Configuration())

        val driver = Http4kWebDriver(app)

        driver.navigate().to("/")
        driver.findElement(By.cssSelector("textarea"))!!.sendKeys(getResourceAsText("simplest.har"))
        driver.findElement(By.cssSelector("form"))!!.submit()
        assertEquals(Status.OK, driver.status)

        assertEquals(transactions, driver.findElement(By.cssSelector("#output"))!!.text)
    }

    @Test
    fun `you can use the index page to submit a form with a curl command in it`() {
        val app = App(Configuration())

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
}

