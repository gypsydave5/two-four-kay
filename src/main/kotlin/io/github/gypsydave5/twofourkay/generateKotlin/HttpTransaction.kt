package io.github.gypsydave5.twofourkay.generateKotlin

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName
import org.http4k.core.HttpTransaction
import org.http4k.core.Request
import org.http4k.core.Response
import java.util.*

fun List<HttpTransaction>.generateKotlin(): String {
    return foldIndexed(FileSpec.builder("", "generated")) { idx, fsb, comm ->
        val ordinal = (idx + 1).toOrdinal()
        fsb.addProperty(
            PropertySpec.Companion.builder("${ordinal}Request", Request::class.asTypeName())
                .addKdoc("${ordinal.capitalize()} Transaction")
                .initializer(comm.request.asCodeBlock())
                .build()
        )
            .addProperty(
                PropertySpec.Companion.builder("${ordinal}Response", Response::class.asTypeName())
                    .initializer(comm.response.asCodeBlock())
                    .build()
            )
    }.build().toString()
}

private fun String.capitalize() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

private fun Int.toOrdinal(): String {
    return when (this) {
        0 -> "zeroth"
        1 -> "first"
        2 -> "second"
        3 -> "third"
        4 -> "fourth"
        5 -> "fifth"
        6 -> "sixth"
        7 -> "seventh"
        8 -> "eighth"
        9 -> "ninth"
        10 -> "tenth"
        11 -> "eleventh"
        12 -> "twelfth"
        13 -> "thirteenth"
        14 -> "fourteenth"
        15 -> "fifteenth"
        16 -> "sixteenth"
        17 -> "seventeenth"
        18 -> "eighteenth"
        19 -> "nineteenth"
        20 -> "twentieth"
        21 -> "twenty-first"
        22 -> "twenty-second"
        23 -> "twenty-third"
        else -> "umpty-${this}nth"
    }
}