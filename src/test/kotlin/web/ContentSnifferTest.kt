package web

import har.getResourceAsText
import io.github.gypsydave5.twofourkay.parse.sniffer.ContentType
import io.github.gypsydave5.twofourkay.parse.sniffer.sniff
import kotlin.test.Test
import kotlin.test.assertEquals

class ContentSnifferTest {

    @Test
    fun `detects HAR content type`() {
        val har = getResourceAsText("simplest.har")
        val contentType = sniff(har)
        assertEquals(ContentType.HAR, contentType)
    }

    @Test
    fun `detects Curl content type`() {
        val har = """curl -X GET http://http4k.com"""
        val contentType = sniff(har)
        assertEquals(ContentType.Curl, contentType)
    }

    @Test
    fun `detects wire Request content type`() {
        val wire = """GET /bob HTTP/1.1"""
        val contentType = sniff(wire)
        assertEquals(ContentType.WireRequest, contentType)
    }

    @Test
    fun `detects wire Response content type`() {
        val wire = """HTTP/1.1 200 OK"""
        val contentType = sniff(wire)
        assertEquals(ContentType.WireResponse, contentType)
    }

    @Test
    fun `tells you when it has no idea what the content type is`() {
        val wire = """I wondered lonely as a cloud"""
        val contentType = sniff(wire)
        assertEquals(ContentType.Unknown, contentType)
    }
}