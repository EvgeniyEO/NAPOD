package com.minaev.apod.presentation.feature.apod_list

import androidx.lifecycle.viewModelScope
import com.minaev.apod.domain.entity.onError
import com.minaev.apod.domain.entity.onSuccess
import com.minaev.apod.domain.usecase.GetApodListUseCase
import com.minaev.apod.domain.usecase.RefreshApodListUseCase
import com.minaev.apod.presentation.feature.BaseViewModel
import com.minaev.apod.presentation.feature.ShowErrorSnackBar
import com.minaev.apod.presentation.mapper.toUI
import com.minaev.apod.presentation.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApodListViewModel @Inject constructor(
    val navigationManager: NavigationManager,
    val getApodListUseCase: GetApodListUseCase,
    val refreshApodListUseCase: RefreshApodListUseCase
): BaseViewModel<ApodListContract.Event, ApodListContract.State, ApodListContract.Effect>() {

    private val apodListCount = 20

    init {
        subscribeApodList()
    }

    override fun setInitialState(): ApodListContract.State {
        return ApodListContract.State(
            false,
            ApodListDataModel.ApodListEmpty
        )
    }

    override fun handleEvents(event: ApodListContract.Event) {
        when(event){
            ApodListContract.Event.OnRefreshData -> {
                refreshApodList()
            }
        }
    }

    private fun refreshApodList() {
        viewModelScope.launch{
            setState { copy(isRefresh = true) }
            refreshApodListUseCase(apodListCount)
                .onError {
                    setState { copy(isRefresh = false) }
                    setEffect {
                        ApodListContract.Effect.SnackBar(ShowErrorSnackBar(it.toUI()))
                    }
                    // Todo snack bar
                }
        }
    }

    private fun subscribeApodList(){
        setState { copy(isRefresh = true) }
        getApodListUseCase(apodListCount)
            .onEach { resource ->
                setState { copy(isRefresh = false) }
                resource
                    .onSuccess{
                        setState {
                            copy(
                                apodData = it.toUiDataModel()
                            )
                        }
                    }.onError {
                        setState {
                            copy(
                                apodData = ApodListDataModel.ApodListError
                            )
                        }
                        // Todo snack bar
                    }
            }.launchIn(viewModelScope)
    }
}