package com.chrrissoft.services.foreground.app

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chrrissoft.services.foreground.Constants.SERVICE
import com.chrrissoft.services.foreground.Constants.WAY
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.*
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.BatteryOptimizationDisabled
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.ExactAlarm
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.OverAppPermission
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification
import com.chrrissoft.services.foreground.usecases.interfaces.StartForegroundByFromBackWayUseCase

@SuppressLint("MissingPermission")
class StartForegroundFromBackWorker(
    ctx: Context,
    params: WorkerParameters,
    private val StartForegroundByFromBackWayUseCase: StartForegroundByFromBackWayUseCase,
) : Worker(ctx, params) {
    override fun doWork(): Result {
        val way = when (inputData.getString(WAY)) {
            ExactAlarm::class.java.name -> ExactAlarm()
            Notification::class.java.name -> Notification()
            OverAppPermission::class.java.name -> OverAppPermission()
            BatteryOptimizationDisabled::class.java.name -> BatteryOptimizationDisabled()
            else -> throw IllegalArgumentException()
        }
        val service = when (inputData.getString(SERVICE)) {
            DataSync::class.java.name -> DataSync()
            Media::class.java.name -> Media()
            PhoneCall::class.java.name -> PhoneCall()
            ShortService::class.java.name -> ShortService()
            SpecialUse::class.java.name -> SpecialUse()
            SystemExempted::class.java.name -> SystemExempted()
            Health::class.java.name -> Health()
            Camera::class.java.name -> Camera()
            Location::class.java.name -> Location()
            Microphone::class.java.name -> Microphone()
            else -> throw IllegalArgumentException()
        }
        StartForegroundByFromBackWayUseCase(service, way)
        return success()
    }
}
