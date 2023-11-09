package com.chrrissoft.services.foreground.entities

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU


sealed class Permission {
    abstract val enabled: Boolean
    open val available: Boolean = true

    data class ManageOwnCalls(
        override val enabled: Boolean = false,
    ) : Permission() {
        override val available = SDK_INT >= O
    }

    data class RecordAudio(
        override val enabled: Boolean = false
    ) : Permission()

    data class AccessFineLocation(
        override val enabled: Boolean = false
    ) : Permission()

    data class AccessCoarseLocation(
        override val enabled: Boolean = false
    ) : Permission()

    data class Camera(
        override val enabled: Boolean = false
    ) : Permission()

    data class HighSamplingRateSensors(
        override val enabled: Boolean = false
    ) : Permission() {
        override val available = SDK_INT >= S
    }

    data class ActivityRecognition(
        override val enabled: Boolean = false
    ) : Permission() {
        override val available = SDK_INT >= Q
    }

    data class BodySensors(
        override val enabled: Boolean = false
    ) : Permission()

    data class ScheduleExactAlarm(
        override val enabled: Boolean = false
    ) : Permission() {
        override val available = SDK_INT >= S
    }

    data class UseExactAlarm(
        override val enabled: Boolean = false
    ) : Permission() {
        override val available = SDK_INT >= TIRAMISU
    }
}
