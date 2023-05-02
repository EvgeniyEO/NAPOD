package com.minaev.apod.presentation.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minaev.apod.presentation.feature.model.UIError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface ViewState

interface ViewEvent

interface ViewSideEffect


sealed interface SnackBarEvent
    //object OnHideSnackBar: SnackBarEvent()
    //data class ShowInfoSnackBar(val message: String): SnackBarEvent()
data class ShowErrorSnackBar(val uiError: UIError): SnackBarEvent


abstract class BaseViewModel<
        Event : ViewEvent,
        UiState : ViewState,
        Effect : ViewSideEffect> : ViewModel()
{
    private val initialState: UiState by lazy { setInitialState() }
    abstract fun setInitialState(): UiState

    // Get Current State
    private val currentState: UiState
        get() = _viewState.value
    private val _viewState = MutableStateFlow(initialState)
    val viewState = _viewState.stateIn(viewModelScope, SharingStarted.Eagerly, initialState)

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        _viewState.update { it.reducer() }
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    abstract fun handleEvents(event: Event)

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}