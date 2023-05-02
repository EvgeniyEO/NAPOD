package com.minaev.apod.presentation.feature.home

import com.minaev.apod.R
import com.minaev.apod.presentation.feature.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor():
    BaseViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect>() {

    override fun setInitialState() = HomeContract.State(
        R.string.apod_list_title
    )

    override fun handleEvents(event: HomeContract.Event) {
        TODO("Not yet implemented")
    }
}