package io.github.gypsydave5.twofourkay.har

import org.http4k.core.HttpTransaction
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

fun HAR.toHttpTransactions(): List<HttpTransaction> = log.entries.map(Entry::toHttpTransaction)
fun Entry.toHttpTransaction() = HttpTransaction(request.toRequest(), response.toResponse(), duration())

private fun Entry.duration() = time.toDuration(DurationUnit.MILLISECONDS).toJavaDuration()
private fun Request.toRequest() = org.http4k.core.Request.parseHar(this)
private fun Response.toResponse() = org.http4k.core.Response.parseHar(this)


