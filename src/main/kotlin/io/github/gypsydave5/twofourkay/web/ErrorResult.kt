package io.github.gypsydave5.twofourkay.web

import org.http4k.template.ViewModel

data class ErrorResult(val message: String? = "") : ViewModel {
    override fun template(): String = "ErrorResult"
}