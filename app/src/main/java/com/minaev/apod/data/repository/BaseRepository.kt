package com.minaev.apod.data.repository

import com.minaev.apod.domain.entity.DomainResource
import com.minaev.apod.domain.entity.Error
import com.minaev.apod.domain.entity.Success
import com.minaev.apod.domain.entity.errors.ErrorEntity
import org.slf4j.LoggerFactory
import java.io.InterruptedIOException

abstract class BaseRepository {

    private val logger = LoggerFactory.getLogger(BaseRepository::class.java)

    protected suspend fun <T> doNetworkRequest(
        request: suspend () -> T
    ): DomainResource<T,ErrorEntity> {
        return try {
            Success(request())
        }catch (cause: Throwable){
            handleThrowable(cause)
        }
    }

    private fun handleThrowable(cause: Throwable): Error<ErrorEntity> {
        logger.error("Network error", cause)
        return when(cause){
            is InterruptedIOException -> Error(ErrorEntity.NetworkError.Timeout)
            else -> {
                Error(ErrorEntity.Unexpected(cause.localizedMessage?:"Unexpected error"))
            }
        }
    }


}