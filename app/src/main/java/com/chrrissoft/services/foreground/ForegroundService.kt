package com.chrrissoft.services.foreground

import android.annotation.SuppressLint
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getService
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.graphics.BitmapFactory.decodeResource
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.app.ServiceCompat.stopForeground
import com.chrrissoft.services.Constants
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_CAMERA_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_DATA_SYNC_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_LOCATION_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_MICROPHONE_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_PHONE_CALL_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_SPECIAL_USE_COMPAT
import com.chrrissoft.services.Constants.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED_COMPAT
import com.chrrissoft.services.R.drawable.service_background_image
import com.chrrissoft.services.R.mipmap.ic_launcher
import com.chrrissoft.services.Util.generalNotification
import com.chrrissoft.services.Util.generateRandom
import com.chrrissoft.services.foreground.StopForegroundService.Companion.EXTRA_SERVICE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt

@AndroidEntryPoint
@SuppressLint("MissingPermission")
abstract class ForegroundService : Service() {
    protected abstract val type: Int
    protected abstract val title: String
    protected abstract val notificationId: Int
    private val destroyNotificationId by lazy { notificationId * nextInt() }

    private var created = false


    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    private val scope = CoroutineScope(Job())

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        generalNotification(
            ctx = this,
            subText = title,
            title = "On destroy",
        ).setTimeoutAfter(2000).build()
            .let { notificationManager.notify(destroyNotificationId, it) }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (created) return START_NOT_STICKY
        val largeImage = decodeResource(resources, service_background_image)
        val notification = generalNotification(ctx = this, title = title)
            .setLargeIcon(largeImage).build()
        startForeground((this), (notificationId), notification, (type))
        scope.launch { launchNotification() }
        created = true
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?) = null

    private suspend fun launchNotification() {
        delay(1000)
        val largeImage = decodeResource(resources, service_background_image)
        val componentName by lazy { ComponentName((this), this::class.java) }
        val stopSelfIntent = Intent((this), StopForegroundService::class.java)
            .putExtra(EXTRA_SERVICE, componentName)
        val action = Action(ic_launcher, ("Stop"), getService((this), (0), stopSelfIntent, FLAG_IMMUTABLE))
        generateRandom().forEach { pair ->
            val notification = generalNotification(ctx = this, title = title)
                .setOnlyAlertOnce(true)
                .setProgress((100), pair.first, (false))
                .setLargeIcon(largeImage)
                .addAction(action)
                .build()
            notificationManager.notify(notificationId, notification)
            delay(pair.second)
        }
        stopForeground((this), ServiceCompat.STOP_FOREGROUND_REMOVE)
        notificationManager.cancel(notificationId)
        stopSelf()
    }

    class LocationForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 3
        override val title = "Location service"
        override val type = FOREGROUND_SERVICE_TYPE_LOCATION_COMPAT
    }

    class CameraForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 1
        override val title = "Camera service"
        override val type = FOREGROUND_SERVICE_TYPE_CAMERA_COMPAT
    }

    class DataSyncForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 5
        override val title = "Data sync service"
        override val type = FOREGROUND_SERVICE_TYPE_DATA_SYNC_COMPAT
    }

    class HealthForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 4
        override val title = "Health service"
        override val type = Constants.FOREGROUND_SERVICE_TYPE_HEALTH_COMPAT
    }

    class MediaForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 6
        override val title = "Media service"
        override val type = FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK_COMPAT
    }

    class PhoneCallForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 7
        override val title = "Phone call service"
        override val type = FOREGROUND_SERVICE_TYPE_PHONE_CALL_COMPAT
    }

    class MicrophoneForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 2
        override val title = "Microphone service"
        override val type = FOREGROUND_SERVICE_TYPE_MICROPHONE_COMPAT
    }

    class ShortForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 8
        override val title = "Short service"
        override val type = FOREGROUND_SERVICE_TYPE_SHORT_SERVICE_COMPAT
    }

    class SpecialUseForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 9
        override val title = "Special use service"
        override val type = FOREGROUND_SERVICE_TYPE_SPECIAL_USE_COMPAT
    }

    class SystemExemptedForegroundService @Inject constructor() : ForegroundService() {
        override val notificationId = 10
        override val title = "System exempted"
        override val type = FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED_COMPAT
    }
}
