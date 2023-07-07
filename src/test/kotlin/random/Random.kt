package random

import org.http4k.appendIfNotBlank
import org.http4k.appendIfPresent
import org.http4k.core.*

fun Request.Companion.random(): Request =
    Request(Method.random(), Uri.random(), randomHttpVersion())
        .body(String.random().orEmpty())
        .headers(List(Int.random(0, 10)) { String.random() to String.random() })

fun Response.Companion.random(): Response =
    Response(Status.random(), randomHttpVersion())
        .body(String.random().orEmpty())
        .headers(List(Int.random(0, 10)) { String.random() to String.random() })

private fun Status.Companion.random(): Status {
    return Status(Int.random(100, 599), String.random(20))
}

private fun randomHttpVersion() = listOf(HttpMessage.HTTP_1_1, HttpMessage.HTTP_2).random()
private fun Uri.Companion.random(): Uri {
    val scheme = randomScheme().orEmpty()
    val authority = randomAuthority().orEmpty()
    val path = String.random().orEmpty()
    val query = String.random()
    val fragment = String.random()

    val s = StringBuilder()
        .appendIfNotBlank(scheme, scheme, ":")
        .appendIfNotBlank(authority, "//", authority)
        .append(
            when {
                authority.isBlank() -> path
                path.isBlank() || path.startsWith("/") -> path
                else -> "/$path"
            }
        )
        .appendIfNotBlank(query, "?", query)
        .appendIfNotBlank(fragment, "#", fragment)
        .toString()

    return of(s)
}

private fun randomScheme(): String = String.random()
private fun randomAuthority(): String {
    val userInfo = String.random().orEmpty()
    val host = String.random()
    val port = randomPort().optional()

    return StringBuilder()
        .appendIfNotBlank(userInfo, userInfo, "@")
        .appendIfNotBlank(host, host)
        .appendIfPresent(port, ":", port.toString())
        .toString()
}

private fun randomPort() = Int.random(1, 65535)
private fun <T> T.optional(): T? = listOf(this, null).random()
fun Int.Companion.random(i: Int, i1: Int): Int = (i..i1).random()
fun String.orEmpty(): String = listOf(this, "").random()
private fun Method.Companion.random(): Method = Method.values().random()
fun String.Companion.random(
    size: Int = 20,
    chars: Iterable<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
): String =
    CharArray(size) { chars.toList().random() }.concatToString()