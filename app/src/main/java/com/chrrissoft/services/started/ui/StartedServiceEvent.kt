package com.chrrissoft.services.started.ui

import com.chrrissoft.services.started.ui.StartedServiceViewModel.EventHandler

sealed interface StartedServiceEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is OnChangeState -> handler.onEvent(event = this)
            is OnStop -> handler.onStop()
            is OnStopForeground -> handler.onStopForeground()
            is OnStartForeground -> handler.onStartForeground()
            is OnStartCommand -> handler.onStarCommand(event = this)
        }
    }

    data class OnChangeState(val data: StartedServiceState) : StartedServiceEvent
    object OnStop : StartedServiceEvent
    object OnStopForeground : StartedServiceEvent
    object OnStartForeground : StartedServiceEvent
    data class OnStartCommand(val data: String, val stop: Boolean) : StartedServiceEvent
}
