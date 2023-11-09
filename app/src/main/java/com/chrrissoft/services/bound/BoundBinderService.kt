package com.chrrissoft.services.bound

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoundBinderService : Service() {
    private val _state = MutableStateFlow(State())
    private val localBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return localBinder
    }

    inner class LocalBinder : Binder() {
        val state = _state.asStateFlow()

        fun updateState(data: String) {
            _state.update { it.copy(data = data) }
        }
    }

    data class State(
        val data: String = "",
    )
}
