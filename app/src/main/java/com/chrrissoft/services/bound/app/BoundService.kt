package com.chrrissoft.services.bound.app

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory.decodeResource
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import com.chrrissoft.services.R.drawable.service_background_image
import com.chrrissoft.services.Util.generalNotification
import com.chrrissoft.services.bound.BoundBinderService
import com.chrrissoft.services.bound.BoundBinderService.LocalBinder
import com.chrrissoft.services.bound.BoundBinderService.State
import com.chrrissoft.services.bound.di.Bounds
import com.chrrissoft.services.shared.ResState
import com.chrrissoft.services.shared.ResState.Success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class BoundService @Inject constructor() : Service() {
    private var localBinder: LocalBinder? = null
    private val scope = CoroutineScope(SupervisorJob())
    private val notificationId = nextInt(100)
    private val serviceConnection = BinderServiceConnection()
    private var flowCollection: Job? = null

    @Inject
    @Bounds.Service
    lateinit var bound: MutableStateFlow<ResState<Boolean>>

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_SERVICE_BIND -> {
                bindService(
                    Intent((this), BoundBinderService::class.java),
                    serviceConnection,
                    BIND_AUTO_CREATE
                )
            }

            ACTION_SERVICE_UNBIND -> {
                unbindService(serviceConnection)
                flowCollection?.cancel()
                bound.update { Success((false)) }
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent) = null

    private fun observeState(state: StateFlow<State>) {
        val ctx = this
        val largeIcon = decodeResource(resources, service_background_image)
        flowCollection = scope.launch(IO) {
            state.collect { state ->
                val notification = generalNotification(ctx, state.data, subText = "Service")
                    .setLargeIcon(largeIcon)
                    .build()
                notificationManagerCompat.notify(notificationId, notification)
            }
        }
    }

    inner class BinderServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            localBinder = service as? LocalBinder ?: return
            bound.update { Success((true)) }
            localBinder?.let { observeState(it.state) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            localBinder = null
        }
    }

    companion object {
        const val ACTION_SERVICE_BIND = "com.chrrissoft.services.ACTION_SERVICE_BIND"
        const val ACTION_SERVICE_UNBIND = "com.chrrissoft.services.ACTION_SERVICE_UNBIND"
    }
}
