package org.luteh.ecommerce.presentation.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Base ViewModel class with state, event, and effect handling.
 * @param initialState The initial state of the ViewModel
 */
abstract class BaseViewModel<State, Event, Effect>(initialState: State) : ViewModel() {
    /**
     * Represents the current state of the ViewModel
     */
    private val _state = MutableStateFlow(initialState)

    /**
     * Exposes the state as a read-only flow
     */
    val state = _state.asStateFlow()

    /**
     * Channel to send one-time events to the UI
     */
    private val _effect = Channel<Effect>(Channel.BUFFERED) // Buffered to handle backpressure

    /**
     * Exposes the effect channel as a receive-only flow
     */
    val effect = _effect.receiveAsFlow()

    /**
     * Abstract function to process incoming events
     * @param event The event to be processed
     */
    abstract fun processEvent(event: Event)

    /**
     * Updates the state of the ViewModel
     * @param reducer A function that takes the current state and returns a new state
     */
    protected fun updateState(reducer: (State) -> State) {
        _state.value = reducer(_state.value)
    }

    /**
     * Sends an effect to the UI
     * @param effect The effect to be sent
     */
    protected fun sendEffect(effect: Effect) {
        _effect.trySend(effect)
    }
}