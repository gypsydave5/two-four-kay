package io.github.gypsydave5.twofourkay.web

import org.http4k.template.ViewModel

data class ParseResult(val kotlinString: String) : ViewModel {
    override fun template(): String = "ParseResult"
}
