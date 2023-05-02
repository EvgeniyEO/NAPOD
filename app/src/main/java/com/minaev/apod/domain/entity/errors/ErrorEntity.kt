package com.minaev.apod.domain.entity.errors

sealed class ErrorEntity{

    data class Unexpected(val error: String): ErrorEntity()

    sealed class NetworkError: ErrorEntity(){
        class Unexpected(val error: String) : ErrorEntity()
        object Timeout : ErrorEntity()
    }
}
