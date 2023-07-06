package io.github.gypsydave5.twofourkay.parse

import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.map
import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import io.github.gypsydave5.twofourkay.parse.har.HAR
import io.github.gypsydave5.twofourkay.parse.har.parseHar
import io.github.gypsydave5.twofourkay.parse.har.toHttpTransactions
import io.github.gypsydave5.twofourkay.parse.wire.parseWire
import org.http4k.core.HttpTransaction
import org.http4k.core.Request

fun parse(input: String): Result<String, Error> =
    when {
        input.startsWith("curl") -> {
            Request.parseCurl(input)
                .map(Request::generateKotlin)
        }

        input.startsWith("{") -> {
            input.parseHar()
                .map(HAR::toHttpTransactions)
                .map(List<HttpTransaction>::generateKotlin)
        }

        else -> input.parseWire()
            .map(Request::generateKotlin)

    }