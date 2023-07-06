package io.github.gypsydave5.twofourkay.parse.wire

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import org.http4k.core.HttpMessage
import org.http4k.core.Method
import org.http4k.core.Parameters
import org.http4k.core.Request
import java.util.*

fun Request.Companion.parseWire(wire: String): Result<HttpMessage, Error> {
    val scanner = Scanner(wire)

    val method = Method.valueOf(scanner.next())
    val path = scanner.next()
    val version = scanner.next()

    scanner.nextLine()

    var headers: Parameters = emptyList()
    while (scanner.hasNextLine()) {
        val line = scanner.nextLine().trim()
        if (line.isBlank()) {
            break // End of headers
        }
        val kv = line.split(": ", limit = 2)
        if (kv.size != 2) {
            return Failure(Error("Invalid header: $line"))
        }
        val (key, value) = kv
        headers = headers + (key to value)
    }

    val body = scanner.allRemainingLines().trimStart()

    return Success(Request(method, path, version).headers(headers).body(body))
}

private fun Scanner.allRemainingLines(): String {
    val builder = StringBuilder()
    while (hasNextLine()) {
        builder.append(nextLine())
        if (hasNextLine()) {
            builder.append('\n')
        }
    }
    return builder.toString()
}