package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import org.http4k.core.Status

internal fun Status.toCodeBlock(): CodeBlock {
    val base = CodeBlock.builder()
    val name = toName()

    if (name != null) {
        base.add("%T.${name}", Status::class.asClassName())
    } else {
        base.add("%T($code, %S)", Status::class.asClassName(), description)
    }
    return base.build()
}

// could hack this by turning the message into the variable name
internal fun Status.toName(): String? = when (this) {
    Status.OK -> "OK"
    Status.CREATED -> "CREATED"
    Status.ACCEPTED -> "ACCEPTED"
    Status.NON_AUTHORITATIVE_INFORMATION -> "NON_AUTHORITATIVE_INFORMATION"
    Status.NO_CONTENT -> "NO_CONTENT"
    Status.RESET_CONTENT -> "RESET_CONTENT"
    Status.PARTIAL_CONTENT -> "PARTIAL_CONTENT"
    Status.MULTIPLE_CHOICES -> "MULTIPLE_CHOICES"
    Status.MOVED_PERMANENTLY -> "MOVED_PERMANENTLY"
    Status.FOUND -> "FOUND"
    Status.SEE_OTHER -> "SEE_OTHER"
    Status.NOT_MODIFIED -> "NOT_MODIFIED"
    Status.USE_PROXY -> "USE_PROXY"
    Status.TEMPORARY_REDIRECT -> "TEMPORARY_REDIRECT"
    Status.PERMANENT_REDIRECT -> "PERMANENT_REDIRECT"
    Status.BAD_REQUEST -> "BAD_REQUEST"
    Status.UNSATISFIABLE_PARAMETERS -> "UNSATISFIABLE_PARAMETERS"
    Status.UNAUTHORIZED -> "UNAUTHORIZED"
    Status.PAYMENT_REQUIRED -> "PAYMENT_REQUIRED"
    Status.FORBIDDEN -> "FORBIDDEN"
    Status.NOT_FOUND -> "NOT_FOUND"
    Status.METHOD_NOT_ALLOWED -> "METHOD_NOT_ALLOWED"
    Status.NOT_ACCEPTABLE -> "NOT_ACCEPTABLE"
    Status.PROXY_AUTHENTICATION_REQUIRED -> "PROXY_AUTHENTICATION_REQUIRED"
    Status.REQUEST_TIMEOUT -> "REQUEST_TIMEOUT"
    Status.CONFLICT -> "CONFLICT"
    Status.GONE -> "GONE"
    Status.LENGTH_REQUIRED -> "LENGTH_REQUIRED"
    Status.PRECONDITION_FAILED -> "PRECONDITION_FAILED"
    Status.REQUEST_ENTITY_TOO_LARGE -> "REQUEST_ENTITY_TOO_LARGE"
    Status.REQUEST_URI_TOO_LONG -> "REQUEST_URI_TOO_LONG"
    Status.UNSUPPORTED_MEDIA_TYPE -> "UNSUPPORTED_MEDIA_TYPE"
    Status.REQUESTED_RANGE_NOT_SATISFIABLE -> "REQUESTED_RANGE_NOT_SATISFIABLE"
    Status.EXPECTATION_FAILED -> "EXPECTATION_FAILED"
    Status.I_M_A_TEAPOT -> "I_M_A_TEAPOT"
    Status.UNPROCESSABLE_ENTITY -> "UNPROCESSABLE_ENTITY"
    Status.UPGRADE_REQUIRED -> "UPGRADE_REQUIRED"
    Status.TOO_MANY_REQUESTS -> "TOO_MANY_REQUESTS"
    Status.INTERNAL_SERVER_ERROR -> "INTERNAL_SERVER_ERROR"
    Status.NOT_IMPLEMENTED -> "NOT_IMPLEMENTED"
    Status.BAD_GATEWAY -> "BAD_GATEWAY"
    Status.SERVICE_UNAVAILABLE -> "SERVICE_UNAVAILABLE"
    Status.CONNECTION_REFUSED -> "CONNECTION_REFUSED"
    Status.UNKNOWN_HOST -> "UNKNOWN_HOST"
    Status.GATEWAY_TIMEOUT -> "GATEWAY_TIMEOUT"
    Status.CLIENT_TIMEOUT -> "CLIENT_TIMEOUT"
    Status.HTTP_VERSION_NOT_SUPPORTED -> "HTTP_VERSION_NOT_SUPPORTED"
    else -> null
}