package com.minaev.apod.presentation.feature.apod

import com.minaev.apod.presentation.feature.ViewEvent
import com.minaev.apod.presentation.feature.ViewSideEffect
import com.minaev.apod.presentation.feature.ViewState

class ApodContract {

    data class State(
        val apodDataModel: ApodDataModel
    ) : ViewState

    sealed class Event : ViewEvent {
        object OnRetryButtonClick: Event()
    }

    sealed class Effect: ViewSideEffect {

    }

}