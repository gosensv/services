package com.chrrissoft.services.foreground.usecases.classes

import android.annotation.SuppressLint
import javax.inject.Inject
import android.app.AlarmManager
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import com.chrrissoft.services.ServicesApp
import androidx.core.app.NotificationManagerCompat
import com.chrrissoft.services.R.mipmap.ic_launcher
import com.chrrissoft.services.Util.generalNotification
import com.chrrissoft.services.Util.getForegroundServiceCompat
import com.chrrissoft.services.Util.startForegroundServiceCompat
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServiceEnableUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWayToStartFromBackUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.StartForegroundByFromBackWayUseCase
import kotlin.random.Random.Default.nextInt

@SuppressLint("MissingPermission")
class StartForegroundByFromBackWayUseCaseImpl @Inject constructor(
    private val app: ServicesApp,
    private val alarmManager: AlarmManager,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val ResolveServiceEnableUseCase: ResolveServiceEnableUseCase,
    private val ResolveWayToStartFromBackUseCase: ResolveWayToStartFromBackUseCase,
) : StartForegroundByFromBackWayUseCase {
    override fun invoke(service: ServiceToStart, way: WayToStartFromBack): Boolean {
        if (!ResolveServiceEnableUseCase(service).enabled) {
            generalNotification(ctx = app, title = "Service type without permission")
                .build().let { notificationManagerCompat.notify(nextInt(), it) }
            return false
        }
        if (!ResolveWayToStartFromBackUseCase(way).enabled) {
            generalNotification(ctx = app, title = "Way not available")
                .build().let { notificationManagerCompat.notify(nextInt(), it) }
            return false
        }
        val intent = Intent(app, service.javaClass)
        val pendingIntent = getForegroundServiceCompat(app, (0), intent, FLAG_IMMUTABLE)
        when (way) {
            is ExactAlarm -> {
                alarmManager.setExact(AlarmManager.RTC, (1000), pendingIntent)
            }

            is Notification -> {
                val notification = generalNotification(ctx = app, title = "Tap button to foreground")
                    .addAction(ic_launcher, ("Start"), pendingIntent)
                    .build()
                notificationManagerCompat.notify(nextInt(), notification)
            }

            is OverAppPermission -> {
                app.startForegroundServiceCompat(intent)
            }

            is BatteryOptimizationDisabled -> {
                app.startForegroundServiceCompat(intent)
            }
        }

        return true
    }
}
