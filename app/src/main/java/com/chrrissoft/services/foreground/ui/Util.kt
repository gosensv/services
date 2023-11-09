package com.chrrissoft.services.foreground.ui

import com.chrrissoft.services.foreground.entities.Permission
import com.chrrissoft.services.foreground.entities.Permission.*
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.*
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification

object Util {
    val WayToStartFromBack.label
        get() = run {
            when (this) {
                is ExactAlarm -> "Exact alarm"
                is OverAppPermission -> "Over app permission"
                is BatteryOptimizationDisabled -> "Not battery optimization"
                is Notification -> "Notification"
            }
        }

    val ServiceToStart.label
        get() = run {
            when (this) {
                is DataSync -> "Data sync"
                is Media -> "Media"
                is PhoneCall -> "Phone call"
                is ShortService -> "Short"
                is SpecialUse -> "Special use"
                is SystemExempted -> "Exempted"
                is ServiceToStart.WhileInUseTypeService.Camera -> "Camera"
                is Health -> "Health"
                is Location -> "Location"
                is Microphone -> "Microphone"
            }
        }

    val Permission.label
        get() = run {
            when (this) {
                is AccessCoarseLocation -> "Access coarse location"
                is AccessFineLocation -> "Access fine location"
                is ActivityRecognition -> "Activity recognition"
                is BodySensors -> "Body sensors"
                is Permission.Camera -> "Camera"
                is HighSamplingRateSensors -> "High sampling rate sensors"
                is ManageOwnCalls -> "Manage own calls"
                is RecordAudio -> "Record audio"
                is ScheduleExactAlarm -> "Schedule exact alarm"
                is UseExactAlarm -> "Use exact alarm"
            }
        }
}
