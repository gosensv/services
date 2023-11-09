package com.chrrissoft.services.bound.ui

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.debug
import com.chrrissoft.services.bound.BoundBinderService
import com.chrrissoft.services.bound.BoundBinderService.LocalBinder
import com.chrrissoft.services.bound.BoundBinderService.State
import com.chrrissoft.services.bound.app.BoundService
import com.chrrissoft.services.bound.app.BoundService.Companion.ACTION_SERVICE_BIND
import com.chrrissoft.services.bound.app.BoundService.Companion.ACTION_SERVICE_UNBIND
import com.chrrissoft.services.bound.di.Bounds
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind.Ctx.*
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnChangeState
import com.chrrissoft.services.bound.ui.BoundServiceEvent.OnChangePage
import com.chrrissoft.services.bound.ui.BoundServiceState.BinderState
import com.chrrissoft.services.bound.ui.BoundServiceState.Page
import com.chrrissoft.services.shared.ResState
import com.chrrissoft.services.shared.ResState.Loading
import com.chrrissoft.services.shared.ResState.Success
import com.chrrissoft.services.shared.SnackbarData
import com.chrrissoft.services.shared.SnackbarData.MessageType.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope as scope

@HiltViewModel
class BoundServiceViewModel @Inject constructor(
    private val app: ServicesApp,
    @Bounds.App
    private val appBound: MutableStateFlow<ResState<Boolean>>,
    @Bounds.Service
    private val serviceBound: MutableStateFlow<ResState<Boolean>>,
) : ViewModel() {
    private val _state = MutableStateFlow(BoundServiceState())
    val stateFlow = _state.asStateFlow()
    val state get() = stateFlow.value
    private val _page get() = state.page
    private val _binder get() = state.binder
    private val _snackbarData get() = state.snackbarData
    private val handler = EventHandler()
    private val serviceConnection = BinderServiceConnection()
    private var localBinder: LocalBinder? = null
    private var flowCollection: Job? = null

    init {
        scope.launch(IO) {
            serviceBound.collect {
                updateState(binder = _binder.copy(serviceBound = it))
            }
        }

        scope.launch(IO) {
            appBound.collect {
                updateState(binder = _binder.copy(appBound = it))
            }
        }
    }

    fun handleEvent(event: BoundServiceEvent) {
        try {
            event.resolve(handler)
        } catch (e: Throwable) {
            updateState(snackbarData = _snackbarData.copy(type = Error))
            viewModelScope.launch {
                _snackbarData.state.showSnackbar(message = "Was occurs an error")
            }
        }
    }

    inner class EventHandler {
        val binderHandler = BinderServiceEventHandler()

        inner class BinderServiceEventHandler {
            fun onEvent(event: OnBind) {
                when (event.data) {
                    is Activity -> {
                        val intent = Intent(app, BoundBinderService::class.java)
                        if (event.bind) event.data.activity
                            .bindService(intent, serviceConnection, BIND_AUTO_CREATE)
                        else event.data.activity.unbindService(serviceConnection)
                            .apply {
                                flowCollection?.cancel()
                                _state.update { it.copy(binder = it.binder.copy(activityBound = Success((false)))) }
                            }
                    }
                    App -> if (event.bind) app.bind() else app.unbind()
                    Service -> {
                        serviceBound.update { Loading }
                        val action = if (event.bind) ACTION_SERVICE_BIND else ACTION_SERVICE_UNBIND
                        app.startService(Intent(app, BoundService::class.java).apply {
                            this.action = action
                        })
                    }
                }
            }

            fun onEvent(event: OnChangeState) {
                localBinder?.updateState(event.data)
            }
        }

        fun onEvent(event: OnChangePage) {
            updateState(page = event.data)
        }
    }

    inner class BinderServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            localBinder = service as LocalBinder
            observeState(localBinder!!.state)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            debug(message = "onServiceDisconnected")
            localBinder = null
        }
    }

    private fun observeState(state: StateFlow<State>) {
        _state.update { it.copy(binder = it.binder.copy(activityBound = Success(true))) }
        flowCollection = scope.launch(IO) {
            state.collect { state ->
                updateState(binder = _state.value.binder.copy(state = state))
            }
        }
    }

    private fun updateState(
        page: Page = _page,
        binder: BinderState = _binder,
        snackbarData: SnackbarData = _snackbarData
    ) {
        _state.update {
            it.copy(page = page, binder = binder, snackbarData = snackbarData)
        }
    }
}
