package com.chrrissoft.services.bound.ui

import androidx.activity.ComponentActivity
import com.chrrissoft.services.bound.ui.BoundServiceState.Page
import com.chrrissoft.services.bound.ui.BoundServiceViewModel.EventHandler
import com.chrrissoft.services.bound.ui.BoundServiceViewModel.EventHandler.BinderServiceEventHandler

sealed interface BoundServiceEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is OnChangePage -> handler.onEvent(event = this)
            is BinderServiceEvent -> resolve(handler.binderHandler)
        }
    }

    sealed interface BinderServiceEvent : BoundServiceEvent {
        fun resolve(handler: BinderServiceEventHandler) {
            when (this) {
                is OnBind -> handler.onEvent(event = this)
                is OnChangeState -> handler.onEvent(event = this)
            }
        }

        data class OnBind(
            val data: Ctx,
            val bind: Boolean,
            val activity: ComponentActivity,
        ): BinderServiceEvent {
            sealed interface Ctx {
                object App : Ctx

                object Service : Ctx

                data class Activity(val activity: ComponentActivity) : Ctx
            }
        }

        data class OnChangeState(val data: String) : BinderServiceEvent
    }

    data class OnChangePage(val data: Page) : BoundServiceEvent
}
