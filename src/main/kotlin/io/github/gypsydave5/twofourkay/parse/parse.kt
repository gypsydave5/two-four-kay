package io.github.gypsydave5.twofourkay.parse

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.map
import io.github.gypsydave5.twofourkay.generateKotlin.generateKotlin
import io.github.gypsydave5.twofourkay.parse.curl.parseCurl
import io.github.gypsydave5.twofourkay.parse.har.HAR
import io.github.gypsydave5.twofourkay.parse.har.parseHar
import io.github.gypsydave5.twofourkay.parse.har.toHttpTransactions
import io.github.gypsydave5.twofourkay.parse.sniffer.ContentType
import io.github.gypsydave5.twofourkay.parse.sniffer.sniff
import io.github.gypsydave5.twofourkay.parse.wire.parseWireRequest
import io.github.gypsydave5.twofourkay.parse.wire.parseWireResponse
import org.http4k.core.HttpTransaction
import org.http4k.core.Request
import org.http4k.core.Response

fun parse(input: String): Result<String, Error> =
    when (sniff(input)) {
        ContentType.Curl -> {
            Request.parseCurl(input)
                .map(Request::generateKotlin)
        }

        ContentType.HAR -> {
            input.parseHar()
                .map(HAR::toHttpTransactions)
                .map(List<HttpTransaction>::generateKotlin)
        }

        ContentType.WireRequest -> input.parseWireRequest()
            .map(Request::generateKotlin)

        ContentType.WireResponse -> input.parseWireResponse()
            .map(Response::generateKotlin)

        ContentType.Unknown -> Failure(java.lang.Error("Unknown content type"))

    }