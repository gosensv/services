package com.chrrissoft.services.foreground.ui

import androidx.activity.ComponentActivity
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.Page
import com.chrrissoft.services.foreground.ui.ForegroundServicesViewModel.EventHandler

sealed interface ForegroundServicesEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            OnServicesPermissionAsk -> handler.onServicesEnablesAsk()
            OnWaysToStartFromBackAsk -> handler.onWaysAks()
            is OnStop -> handler.onEvent(event = this)
            is OnStart -> handler.onEvent(event = this)
            is OnStartFromBack -> handler.onEvent(event = this)
            is OnChangePage -> handler.onEvent(event = this)
            is OnForegroundAppRegistration -> handler.onEvent(event = this)
        }
    }


    object OnServicesPermissionAsk : ForegroundServicesEvent

    object OnWaysToStartFromBackAsk : ForegroundServicesEvent

    data class OnChangePage(val data: Page) : ForegroundServicesEvent

    data class OnStop(val service: ServiceToStart) : ForegroundServicesEvent

    data class OnStart(val service: ServiceToStart) : ForegroundServicesEvent

    data class OnStartFromBack(val way: WayToStartFromBack, val service: ServiceToStart)
        : ForegroundServicesEvent


    data class OnForegroundAppRegistration(val data: Boolean, val ctx: ComponentActivity) :
        ForegroundServicesEvent
}
