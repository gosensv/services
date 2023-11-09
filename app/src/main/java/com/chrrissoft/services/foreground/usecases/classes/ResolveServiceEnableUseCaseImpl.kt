package com.chrrissoft.services.foreground.usecases.classes

import android.Manifest.permission.*
import android.app.AlarmManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.canScheduleExactAlarmsCompact
import com.chrrissoft.services.Util.hasPermission
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.*
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.*
import com.chrrissoft.services.foreground.usecases.interfaces.ResolvePermissionsEnableUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServiceEnableUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ResolveServiceEnableUseCaseImpl @Inject constructor(
    private val app: ServicesApp,
    private val alarmManager: AlarmManager,
    private val ResolvePermissionsEnableUseCase: ResolvePermissionsEnableUseCase,
) : ResolveServiceEnableUseCase {
    override fun <T : ServiceToStart> invoke(service: T): T {
        val permissions = ResolvePermissionsEnableUseCase(service.permissions)
        return when (service) {
            is Media -> service.copy(enabled = true)
            is DataSync -> service.copy(enabled = true)
            is SpecialUse -> service.copy(enabled = true)
            is ShortService -> service.copy(enabled = true)
            is Camera -> {
                service.copy(permissions = permissions, enabled = app.hasPermission(CAMERA))
            }
            is Health -> {
                val highSamplingRateSensors = run {
                    if (SDK_INT >= S)
                        app.hasPermission(HIGH_SAMPLING_RATE_SENSORS) else false
                }
                val activityRecognition = run {
                    if (SDK_INT >= Q) app.hasPermission(ACTIVITY_RECOGNITION)
                    else false
                }
                val bodySensors = app.hasPermission(BODY_SENSORS)
                service.copy(permissions = permissions, enabled = highSamplingRateSensors || activityRecognition && bodySensors)
            }
            is Location -> {
                val fine = app.hasPermission(ACCESS_FINE_LOCATION)
                val coarse = app.hasPermission(ACCESS_COARSE_LOCATION)
                service.copy(permissions = permissions, enabled = fine || coarse)
            }
            is PhoneCall -> {
                val enable = if (SDK_INT >= O) app.hasPermission(MANAGE_OWN_CALLS) else false
                service.copy(permissions = permissions, enabled = enable)
            }
            is Microphone -> {
                service.copy(permissions = permissions, enabled = app.hasPermission(RECORD_AUDIO))
            }
            is SystemExempted -> {
                val enabled = alarmManager.canScheduleExactAlarmsCompact()
                service.copy(permissions = permissions, enabled = enabled)
            }
            else -> throw IllegalAccessError()
        } as T
    }
}
