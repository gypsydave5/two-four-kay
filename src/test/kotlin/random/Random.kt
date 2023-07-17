package random

import org.http4k.appendIfNotBlank
import org.http4k.appendIfPresent
import org.http4k.core.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RandomContextTest {

    @Test
    fun `do it`() {
        repeat(2000) {
            randomly {
                assertNotEquals(Int.random(-1000000000, 1000000000), Int.random(-1000000000, 1000000000))
            }

            with(fixedRandom) {
                assertEquals(Int.random(-1000000000, 1000000000), Int.random(-1000000000, 1000000000))
            }

            val seed = 0L
            val x = randomly(seed) { Int.random(-1000000000, 1000000000) }
            val y = randomly(seed) { Int.random(-1000000000, 1000000000) }
            assertEquals(x, y)
        }
    }

    private val fixedRandom = object : Random() {
        override fun nextBits(bitCount: Int): Int = 0
    }
}

fun <T> randomly(block: Random.() -> T): T {
    val seed = System.currentTimeMillis()
    println("Random seed: $seed")
    return randomly(seed, block)
}

fun <T> randomly(seed: Long, block: Random.() -> T): T {
    val random = Random(seed)
    return with(random, block)
}

fun <T> repeatRandomly(count: Int, block: Random.() -> T): List<T> =
    randomly { (0 to count).toList().map { block() } }

context(Random) fun Request.Companion.random(): Request =
    Request(Method.random(), Uri.random(), randomHttpVersion())
        .body(String.random().orEmpty())
        .headers(List(Int.random(0, 10)) { String.random() to String.random() })

context(Random) fun Response.Companion.random(): Response =
    Response(Status.random(), randomHttpVersion())
        .body(String.random().orEmpty())
        .headers(List(Int.random(0, 10)) { String.random() to String.random() })

context (Random) private fun Status.Companion.random(): Status = Status(Int.random(100, 599), String.random(20))

context (Random) fun randomHttpVersion() = listOf(HttpMessage.HTTP_1_1, HttpMessage.HTTP_2).random()

context (Random) fun Uri.Companion.random(): Uri {
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

context (Random) private fun randomScheme(): String = String.random()

context (Random) private fun randomAuthority(): String {
    val userInfo = String.random().orEmpty()
    val host = String.random()
    val port = randomPort().optional()

    return StringBuilder()
        .appendIfNotBlank(userInfo, userInfo, "@")
        .appendIfNotBlank(host, host)
        .appendIfPresent(port, ":", port.toString())
        .toString()
}

context (Random) fun randomPort() = Int.random(1, 65535)

context (Random) fun <T> T.optional(): T? = listOf(this, null).random(this@Random)

context (Random) fun Int.Companion.random(lower: Int, upper: Int): Int = (lower..upper).random(this@Random)

context (Random) fun String.orEmpty(): String = listOf(this, "").random()

context (Random) fun Method.Companion.random(): Method = Method.values().random(this@Random)

context (Random) fun String.Companion.random(
    size: Int = 20,
    chars: Iterable<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
): String =
    CharArray(size) { chars.toList().random(this@Random) }.concatToString()

