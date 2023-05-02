package com.minaev.apod.domain.entity

sealed interface DomainResource<out T, out R>

class Success<out T>(val value: T): DomainResource<T, Nothing>
class Error<out R>(val value: R) : DomainResource<Nothing, R>

suspend fun <T,R> DomainResource<T, R>.onSuccess(
    executable: suspend (T) -> Unit
): DomainResource<T, R> = apply {
    if (this is Success<T>) {
        executable(value)
    }
}

suspend fun <T,R> DomainResource<T,R>.onError(
    executable: suspend (R) -> Unit
): DomainResource<T,R> = apply {
    if (this is Error<R>) {
        executable(value)
    }
}
