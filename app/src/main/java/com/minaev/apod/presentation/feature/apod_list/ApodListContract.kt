package com.minaev.apod.presentation.feature.apod_list

import com.minaev.apod.presentation.feature.SnackBarEvent
import com.minaev.apod.presentation.feature.ViewEvent
import com.minaev.apod.presentation.feature.ViewSideEffect
import com.minaev.apod.presentation.feature.ViewState

class ApodListContract {

    data class State(
        val isRefresh: Boolean,
        val apodData: ApodListDataModel
    ) : ViewState

    sealed class Event : ViewEvent {
        object OnRefreshData: Event()
    }

    sealed class Effect: ViewSideEffect {
        data class SnackBar(val snackBarEvent: SnackBarEvent): Effect()
    }

}