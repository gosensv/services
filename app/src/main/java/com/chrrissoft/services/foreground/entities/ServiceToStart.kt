package com.chrrissoft.services.foreground.entities

import com.chrrissoft.services.foreground.ForegroundService.*

sealed interface ServiceToStart {
    val enabled: Boolean
    val javaClass: Class<*>
    val permissions: List<Permission> get() = emptyList()

    sealed interface NormalTypeService : ServiceToStart {
        data class DataSync(override val enabled: Boolean = false) :
            NormalTypeService {
            override val javaClass: Class<*> =
                DataSyncForegroundService::class.java
        }

        data class Media(override val enabled: Boolean = false) :
            NormalTypeService {
            override val javaClass: Class<*> = MediaForegroundService::class.java
        }

        data class PhoneCall(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.ManageOwnCalls())
            }
        ) :
            NormalTypeService {
            override val javaClass: Class<*> = PhoneCallForegroundService::class.java
        }

        data class ShortService(override val enabled: Boolean = false) :
            NormalTypeService {
            override val javaClass: Class<*> = ShortForegroundService::class.java
        }

        data class SpecialUse(override val enabled: Boolean = false) :
            NormalTypeService {
            override val javaClass: Class<*> = SpecialUseForegroundService::class.java
        }

        data class SystemExempted(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.ScheduleExactAlarm())
                add(Permission.UseExactAlarm())
            }
        ) : NormalTypeService {
            override val javaClass: Class<*> =
                SystemExemptedForegroundService::class.java
        }
    }

    sealed interface WhileInUseTypeService : ServiceToStart {
        data class Health(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.HighSamplingRateSensors())
                add(Permission.BodySensors())
                add(Permission.ActivityRecognition())
            },
        ) : WhileInUseTypeService {
            override val javaClass: Class<*> = HealthForegroundService::class.java
        }

        data class Camera(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.Camera())
            },
        ) : WhileInUseTypeService {
            override val javaClass: Class<*> = CameraForegroundService::class.java
        }

        data class Location(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.AccessCoarseLocation())
                add(Permission.AccessFineLocation())
            },
        ) : WhileInUseTypeService {
            override val javaClass: Class<*> = LocationForegroundService::class.java
        }

        data class Microphone(
            override val enabled: Boolean = false,
            override val permissions: List<Permission> = buildList {
                add(Permission.RecordAudio())
            },
        ) : WhileInUseTypeService {
            override val javaClass: Class<*> = MicrophoneForegroundService::class.java
        }
    }
}
