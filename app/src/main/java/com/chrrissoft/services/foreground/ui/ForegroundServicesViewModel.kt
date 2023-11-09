package com.chrrissoft.services.foreground.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest.Builder
import androidx.work.WorkManager
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.registerNotExportedReceiver
import com.chrrissoft.services.Util.startForegroundServiceCompat
import com.chrrissoft.services.foreground.Constants.SERVICE
import com.chrrissoft.services.foreground.Constants.WAY
import com.chrrissoft.services.foreground.app.StartForegroundFromBackWorker
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnChangePage
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnForegroundAppRegistration
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStart
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStartFromBack
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStop
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnWaysToStartFromBackAsk
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.ForegroundServicesNormalTypesState
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.ForegroundServicesWhileInUseTypesState
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.Page
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServiceEnableUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServicesEnableUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWayToStartFromBackUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWaysToStartFromBackUseCase
import com.chrrissoft.services.shared.SnackbarData
import com.chrrissoft.services.shared.SnackbarData.MessageType.Error
import com.chrrissoft.services.shared.SnackbarData.MessageType.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import kotlin.system.exitProcess

@SuppressLint("UnspecifiedRegisterReceiverFlag")
@HiltViewModel
class ForegroundServicesViewModel @Inject constructor(
    private val app: ServicesApp,
    private val workManager: WorkManager,
    private val ResolveServiceEnableUseCase: ResolveServiceEnableUseCase,
    private val ResolveServicesEnableUseCase: ResolveServicesEnableUseCase,
    private val ResolveWayToStartFromBackUseCase: ResolveWayToStartFromBackUseCase,
    private val ResolveWaysToStartFromBackUseCase: ResolveWaysToStartFromBackUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ForegroundServicesState())
    val stateFlow = _state.asStateFlow()
    private val state get() = stateFlow.value
    private val _page get() = state.page
    private val _normalTypes get() = state.normalTypes
    private val _whileInUseTypes get() = state.whileInUseTypes
    private val _snackbarData get() = state.snackbarData
    private val handler = EventHandler()
    private val onAppForegroundReceiver = OnAppForegroundReceiver()

    fun handleEvent(event: ForegroundServicesEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        fun onServicesEnablesAsk() {
            val normalTypes = _normalTypes
                .copy(services = ResolveServicesEnableUseCase(_normalTypes.services))
            val whileInUseTypes = _whileInUseTypes
                .copy(services = ResolveServicesEnableUseCase(_whileInUseTypes.services))
            updateState(normalTypes = normalTypes, whileInUseTypes = whileInUseTypes)
        }

        fun onWaysAks() {
            val normal = ResolveWaysToStartFromBackUseCase(_normalTypes.wayToStartFromBack)
            val whileInUse = ResolveWaysToStartFromBackUseCase(_whileInUseTypes.wayToStartFromBack)
            updateState(
                normalTypes = _normalTypes.copy(wayToStartFromBack = normal),
                whileInUseTypes = _whileInUseTypes.copy(wayToStartFromBack = whileInUse),
            )
        }

        fun onEvent(event: OnStop) {
            app.stopService(Intent(app, event.service.javaClass))
        }

        fun onEvent(event: OnStart) {
            if (!ResolveServiceEnableUseCase(event.service).enabled) {
                updateState(snackbarData = _snackbarData.copy(type = Error))
                viewModelScope.launch {
                    _snackbarData.state.showSnackbar(message = "Service type without permission")
                }
                return
            }
            app.startForegroundServiceCompat(Intent(app, event.service.javaClass))
        }

        fun onEvent(event: OnStartFromBack) {
            if (!ResolveServiceEnableUseCase(event.service).enabled) {
                updateState(snackbarData = _snackbarData.copy(type = Error))
                viewModelScope.launch {
                    _snackbarData.state.showSnackbar(message = "Service type without permission")
                }
                return
            }
            if (!ResolveWayToStartFromBackUseCase(event.way).enabled) {
                updateState(snackbarData = _snackbarData.copy(type = Error))
                viewModelScope.launch {
                    _snackbarData.state.showSnackbar(message = "Selected way not enabled")
                }
                return
            }
            val workRequest = run {
                Builder(StartForegroundFromBackWorker::class.java)
                    .setInitialDelay(duration = 5, timeUnit = SECONDS)
            }
            val data = run {
                Data.Builder()
                    .putString(WAY, event.way::class.java.name)
                    .putString(SERVICE, event.service::class.java.name)
            }
            workRequest.setInputData(data.build())
            workManager.enqueue(workRequest.build())
            viewModelScope.launch {
                updateState(snackbarData = _snackbarData.copy(type = Success))
                viewModelScope.launch {
                    _snackbarData.state.showSnackbar(message = "App will close soon")
                }
                delay((3000))
                exitProcess(0)
            }
        }

        fun onEvent(event: OnChangePage) {
            updateState(page = event.data)
        }

        fun onEvent(event: OnForegroundAppRegistration) {
            val filter = IntentFilter(ACTION_ON_APP_FOREGROUND)
            if (event.data) event.ctx.registerNotExportedReceiver(onAppForegroundReceiver, filter)
            else event.ctx.unregisterReceiver(onAppForegroundReceiver)
        }
    }

    private inner class OnAppForegroundReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != ACTION_ON_APP_FOREGROUND) return
            handleEvent(OnWaysToStartFromBackAsk)
        }
    }

    private fun updateState(
        page: Page = _page,
        snackbarData: SnackbarData = _snackbarData,
        normalTypes: ForegroundServicesNormalTypesState = _normalTypes,
        whileInUseTypes: ForegroundServicesWhileInUseTypesState = _whileInUseTypes,
    ) {
        _state.update {
            it.copy(
                page = page,
                snackbarData = snackbarData,
                normalTypes = normalTypes,
                whileInUseTypes = whileInUseTypes,
            )
        }
    }

    companion object {
        const val ACTION_ON_APP_FOREGROUND = "com.chrrissoft.services.ACTION_ON_APP_FOREGROUND"
    }
}
