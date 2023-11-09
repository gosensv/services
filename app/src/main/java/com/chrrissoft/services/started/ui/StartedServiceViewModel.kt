package com.chrrissoft.services.started.ui

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.lifecycle.ViewModel
import com.chrrissoft.services.Constants.ACTION_START_FOREGROUND_SERVICE
import com.chrrissoft.services.Constants.ACTION_STOP_FOREGROUND_SERVICE
import com.chrrissoft.services.Constants.EXTRA_STOP_SERVICE_AFTER_WORK
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.putData
import com.chrrissoft.services.started.StartedService
import com.chrrissoft.services.started.ui.StartedServiceEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StartedServiceViewModel @Inject constructor(private val app: ServicesApp) : ViewModel() {
    private val _state = MutableStateFlow(StartedServiceState())
    val stateFlow = _state.asStateFlow()
    private val handler = EventHandler()

    fun handleEvent(event: StartedServiceEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        fun onEvent(event: OnChangeState) {
            _state.update { event.data }
        }

        fun onStop() {
            app.stopService(Intent(app, StartedService::class.java))
        }

        fun onStopForeground() {
            Intent(app, StartedService::class.java).let {
                it.action = ACTION_STOP_FOREGROUND_SERVICE
                app.startService(it)
            }
        }

        fun onStartForeground() {
            Intent(app, StartedService::class.java).let {
                it.action = ACTION_START_FOREGROUND_SERVICE
                if (SDK_INT < O) app.startService(it)
                else app.startForegroundService(it)
            }
        }

        fun onStarCommand(event: OnStartCommand) {
            Intent(app, StartedService::class.java).putData(event.data).let {
                it.putExtra(EXTRA_STOP_SERVICE_AFTER_WORK, event.stop)
                app.startService(it)
            }
        }
    }
}
