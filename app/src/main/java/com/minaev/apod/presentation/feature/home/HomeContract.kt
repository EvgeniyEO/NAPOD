package com.minaev.apod.presentation.feature.home

import com.minaev.apod.presentation.feature.ViewEvent
import com.minaev.apod.presentation.feature.ViewSideEffect
import com.minaev.apod.presentation.feature.ViewState

class HomeContract {

    data class State(
        val headerTitle: Int
    ) : ViewState

    sealed class Event : ViewEvent {
    }

    sealed class Effect: ViewSideEffect {
    }

}