package com.chrrissoft.services.started

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.app.ServiceCompat.stopForeground
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_SPECIAL_USE_COMPAT
import com.chrrissoft.services.Util.actionStartForeground
import com.chrrissoft.services.Util.actionStopForeground
import com.chrrissoft.services.Util.extraStopAfterWork
import com.chrrissoft.services.Util.generalNotification
import com.chrrissoft.services.Util.getData
import com.chrrissoft.services.started.StartedServiceDependency.OnFinishedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class StartedService : Service() {
    @Inject
    lateinit var dependency: StartedServiceDependency

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    private var created = false
    private var finishedListener: OnFinishedListener? = null


    override fun onCreate() {
        super.onCreate()
        generalNotification(
            ctx = this,
            title = "On create",
            subText = "Started service"
        ).build().let { notificationManagerCompat.notify(CREATE_NOTIFICATION_ID, it) }
    }

    override fun onDestroy() {
        dependency.stopWork()
        finishedListener?.let { dependency.removeOnFinishedListener(it) }
        generalNotification(
            ctx = this,
            title = "On destroy",
            subText = "Started service"
        ).build().let { notificationManagerCompat.notify(DESTROY_NOTIFICATION_ID, it) }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.actionStopForeground) {
            stopForeground((this), ServiceCompat.STOP_FOREGROUND_REMOVE)
            if (!created) stopSelf()
            return START_NOT_STICKY
        }
        if (intent.actionStartForeground) {
            val notification  =  generalNotification((this), ("Foreground")).build()
            startForeground((this), FOREGROUND_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE_COMPAT)
            return START_NOT_STICKY
        }

        if (intent.extraStopAfterWork) {
            finishedListener = OnFinishedListener { stopSelfResult(startId) }
            dependency.addOnFinishedListener(finishedListener!!)
        }
        dependency.startWork(intent.getData, startId)
        created = true
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?) = null

    private companion object {
        private const val FOREGROUND_ID = 30
        private const val DESTROY_NOTIFICATION_ID = 10
        private const val CREATE_NOTIFICATION_ID = DESTROY_NOTIFICATION_ID * 2
    }
}
