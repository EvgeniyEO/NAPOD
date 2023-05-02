package com.minaev.apod.presentation.feature.model

sealed class UIError{
    data class ErrorString(val message: String): UIError()
    data class ErrorResource(val resId: Int): UIError()
}
