package com.chrrissoft.services

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory.decodeResource
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat.Builder
import androidx.core.app.NotificationManagerCompat
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import com.chrrissoft.services.Constants.GENERAL_NOTIFICATIONS_CHANNEL_ID
import com.chrrissoft.services.R.drawable.service_background_image
import com.chrrissoft.services.Util.generalNotification
import com.chrrissoft.services.app.ServicesAppWorkerFactory
import com.chrrissoft.services.bound.BoundBinderService
import com.chrrissoft.services.bound.BoundBinderService.State
import com.chrrissoft.services.bound.di.Bounds
import com.chrrissoft.services.shared.ResState
import com.chrrissoft.services.shared.ResState.Loading
import com.chrrissoft.services.shared.ResState.Success
import dagger.hilt.android.HiltAndroidApp
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

@HiltAndroidApp
@SuppressLint("MissingPermission")
class ServicesApp : Application(), Provider {
    private val serviceConnection = BinderServiceConnection()
    private var localBinder: BoundBinderService.LocalBinder? = null
    private var flowCollection: Job? = null
    private val scope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var workerFactory: ServicesAppWorkerFactory

    @Inject
    @Bounds.App
    lateinit var bound: MutableStateFlow<ResState<Boolean>>

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        if (SDK_INT < O) return
        Builder(GENERAL_NOTIFICATIONS_CHANNEL_ID, IMPORTANCE_HIGH)
            .setName("General Notification")
            .build()
            .let { notificationManagerCompat.createNotificationChannel(it) }
    }

    fun bind() {
        bindService(
            Intent((this), BoundBinderService::class.java),
            serviceConnection, BIND_AUTO_CREATE
        )
        bound.update { Loading }
    }

    fun unbind() {
        unbindService(serviceConnection)
        flowCollection?.cancel()
        bound.update { Success((false)) }
    }

    inner class BinderServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            localBinder = service as BoundBinderService.LocalBinder
            observeState(localBinder!!.state)
            bound.update { Success((true)) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            localBinder = null
        }
    }

    private fun observeState(state: StateFlow<State>) {
        val ctx = this
        val notificationId = nextInt()
        val largeIcon = decodeResource(resources, service_background_image)
        flowCollection = scope.launch(IO) {
            state.collect { state ->
                val notification = generalNotification(ctx, state.data, subText = "App")
                    .setLargeIcon(largeIcon)
                    .build()
                notificationManagerCompat.notify(notificationId, notification)
            }
        }
    }

}
