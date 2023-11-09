package com.chrrissoft.services.foreground.entities

sealed interface WayToStartFromBack {
    val enabled: Boolean
    val available: Boolean

    sealed interface NormalTypes : WayToStartFromBack {
        data class ExactAlarm(
            override val enabled: Boolean = false,
            override val available: Boolean = false,
        ) : NormalTypes

        data class OverAppPermission(
            override val enabled: Boolean = false,
            override val available: Boolean = true,
        ) : NormalTypes

        data class BatteryOptimizationDisabled(
            override val enabled: Boolean = false,
            override val available: Boolean = true,
        ) : NormalTypes
    }

    sealed interface WhileInUseTypes : WayToStartFromBack

    data class Notification(
        override val enabled: Boolean = false,
        override val available: Boolean = false,
    ) : NormalTypes, WhileInUseTypes
}
