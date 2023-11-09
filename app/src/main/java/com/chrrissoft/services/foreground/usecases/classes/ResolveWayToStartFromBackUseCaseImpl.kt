package com.chrrissoft.services.foreground.usecases.classes

import android.app.AlarmManager
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.canScheduleExactAlarmsCompact
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWayToStartFromBackUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ResolveWayToStartFromBackUseCaseImpl @Inject constructor(
    private val app: ServicesApp,
    private val alarmManager: AlarmManager,
    private val powerManager: PowerManager,
    private val notificationManagerCompat: NotificationManagerCompat,
) : ResolveWayToStartFromBackUseCase {
    override fun <T : WayToStartFromBack> invoke(way: T): T {
        return when (way) {
            is ExactAlarm -> way.copy(enabled = alarmManager.canScheduleExactAlarmsCompact())
            is OverAppPermission -> way.copy(enabled = Settings.canDrawOverlays(app))
            is BatteryOptimizationDisabled -> way.copy(enabled = powerManager.isIgnoringBatteryOptimizations(app.packageName))
            is Notification -> way.copy(enabled = notificationManagerCompat.areNotificationsEnabled())
            else -> throw IllegalArgumentException()
        } as T
    }
}
