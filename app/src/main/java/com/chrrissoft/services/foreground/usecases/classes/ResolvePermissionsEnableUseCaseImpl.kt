package com.chrrissoft.services.foreground.usecases.classes

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BODY_SENSORS
import android.Manifest.permission.CAMERA
import android.Manifest.permission.RECORD_AUDIO
import android.app.AlarmManager
import com.chrrissoft.services.Constants.ACTIVITY_RECOGNITION_COMPAT
import com.chrrissoft.services.Constants.HIGH_SAMPLING_RATE_SENSORS_COMPAT
import com.chrrissoft.services.Constants.MANAGE_OWN_CALLS_COMPAT
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.canScheduleExactAlarmsCompact
import com.chrrissoft.services.Util.hasPermission
import com.chrrissoft.services.foreground.entities.Permission
import com.chrrissoft.services.foreground.entities.Permission.AccessCoarseLocation
import com.chrrissoft.services.foreground.entities.Permission.AccessFineLocation
import com.chrrissoft.services.foreground.entities.Permission.ActivityRecognition
import com.chrrissoft.services.foreground.entities.Permission.BodySensors
import com.chrrissoft.services.foreground.entities.Permission.Camera
import com.chrrissoft.services.foreground.entities.Permission.HighSamplingRateSensors
import com.chrrissoft.services.foreground.entities.Permission.ManageOwnCalls
import com.chrrissoft.services.foreground.entities.Permission.RecordAudio
import com.chrrissoft.services.foreground.entities.Permission.ScheduleExactAlarm
import com.chrrissoft.services.foreground.entities.Permission.UseExactAlarm
import com.chrrissoft.services.foreground.usecases.interfaces.ResolvePermissionsEnableUseCase
import javax.inject.Inject

class ResolvePermissionsEnableUseCaseImpl @Inject constructor(
    private val app: ServicesApp,
    private val alarmManager: AlarmManager,
) : ResolvePermissionsEnableUseCase {
    override fun invoke(services: List<Permission>): List<Permission> {
        return services.map {
            when (it) {
                is Camera -> it.copy(enabled = app.hasPermission(CAMERA))
                is RecordAudio -> it.copy(enabled = app.hasPermission(RECORD_AUDIO))
                is BodySensors -> it.copy(enabled = app.hasPermission(BODY_SENSORS))
                is ManageOwnCalls -> it.copy(enabled = app.hasPermission(MANAGE_OWN_CALLS_COMPAT))
                is UseExactAlarm -> it.copy(enabled = alarmManager.canScheduleExactAlarmsCompact())
                is AccessFineLocation -> it.copy(enabled = app.hasPermission(ACCESS_FINE_LOCATION))
                is AccessCoarseLocation -> it.copy(enabled = app.hasPermission(ACCESS_COARSE_LOCATION))
                is ScheduleExactAlarm -> it.copy(enabled = alarmManager.canScheduleExactAlarmsCompact())
                is ActivityRecognition -> it.copy(enabled = app.hasPermission(ACTIVITY_RECOGNITION_COMPAT))
                is HighSamplingRateSensors -> it.copy(enabled = app.hasPermission(HIGH_SAMPLING_RATE_SENSORS_COMPAT))
            }
        }
    }
}

