package com.minaev.apod.presentation.feature.apod

import androidx.lifecycle.viewModelScope
import com.minaev.apod.domain.entity.Error
import com.minaev.apod.domain.entity.Success
import com.minaev.apod.domain.usecase.GetApodUseCase
import com.minaev.apod.presentation.feature.BaseViewModel
import com.minaev.apod.presentation.mapper.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApodViewModel @Inject constructor(
    private val getApodUseCase: GetApodUseCase
): BaseViewModel<ApodContract.Event, ApodContract.State, ApodContract.Effect>() {

    init {
        getApod()
    }

    override fun setInitialState() = ApodContract.State(
        apodDataModel = ApodDataModel.Loading
    )

    override fun handleEvents(event: ApodContract.Event) {
        when(event){
            ApodContract.Event.OnRetryButtonClick -> getApod()
        }
    }

    private fun getApod(){
        setState { copy(apodDataModel = ApodDataModel.Loading) }
        viewModelScope.launch {
            when(val result = getApodUseCase()){
                is Success -> {
                    setState {
                        copy(
                            apodDataModel = ApodDataModel.ApodData(
                                result.value
                            )
                        )
                    }
                }
                is Error -> {
                    setState {
                        copy(
                            apodDataModel = ApodDataModel.Error(
                                result.value.toUI()
                            )
                        )
                    }
                }
            }
        }
    }
}