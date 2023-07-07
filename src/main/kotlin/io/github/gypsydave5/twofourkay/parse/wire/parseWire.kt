package io.github.gypsydave5.twofourkay.parse.wire

import dev.forkhandles.result4k.*
import org.http4k.core.*
import java.util.*

fun Request.Companion.parseWire(wire: String): Result<Request, Error> {
    val scanner = Scanner(wire)

    val method = Method.valueOf(scanner.next())
    val path = scanner.next()
    val version = scanner.nextLine().trim()
    val headers = scanner.headers().onFailure { return it }
    val body = scanner.allRemainingLines().trimStart()

    return Success(Request(method, path, version).headers(headers).body(body))
}

fun Response.Companion.parseWire(wire: String): Result<Response, Error> {
    val scanner = Scanner(wire)

    val statusLine = scanner.nextLine().trim()
    val (httpVersion, statusCodeString, statusMessage) = statusLine.split(" ", limit = 3)
    val statusCode = statusCodeString.asStatusCode().onFailure { return it }

    val status = Status(statusCode, statusMessage)
    val headers = scanner.headers().onFailure { return it }
    val body = scanner.allRemainingLines().trimStart()

    return Success(Response(status, httpVersion).headers(headers).body(body))
}

data class InvalidResponse(override val message: String? = null) : Error(message)
data class InvalidStatusCode(override val message: String? = null) : Error(message)

fun String.parseWireRequest(): Result<Request, Error> = Request.parseWire(this)
fun String.parseWireResponse(): Result<Response, Error> = Response.parseWire(this)

private fun String.asStatusCode(): Result<Int, Error> {
    return resultFrom { toInt() }
        .mapFailure { InvalidStatusCode(this) }
}

private fun Scanner.headers(): Result<Parameters, Error> {
    var headers: Parameters = emptyList()
    while (hasNextLine()) {
        val line = nextLine().trim()
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

    return Success(headers)
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