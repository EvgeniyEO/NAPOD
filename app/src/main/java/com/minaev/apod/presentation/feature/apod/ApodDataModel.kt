package com.minaev.apod.presentation.feature.apod

import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.presentation.feature.model.UIError

sealed class ApodDataModel {
    data class ApodData(val apodEntity: ApodEntity): ApodDataModel()
    object Loading: ApodDataModel()
    data class Error(val uiError: UIError): ApodDataModel()
}