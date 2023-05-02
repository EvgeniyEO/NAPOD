package com.minaev.apod.presentation.mapper

import com.minaev.apod.R
import com.minaev.apod.domain.entity.errors.ErrorEntity
import com.minaev.apod.presentation.feature.model.UIError

fun ErrorEntity.toUI(): UIError {
    return when(this){
        ErrorEntity.NetworkError.Timeout -> UIError.ErrorResource(R.string.error_message_timeout)
        is ErrorEntity.Unexpected -> UIError.ErrorString(this.error)
        is ErrorEntity.NetworkError.Unexpected -> UIError.ErrorString(this.error)
    }
}