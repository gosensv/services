package com.chrrissoft.services.app

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.chrrissoft.services.foreground.app.StartForegroundFromBackWorker
import com.chrrissoft.services.foreground.usecases.interfaces.StartForegroundByFromBackWayUseCase
import javax.inject.Inject

class ServicesAppWorkerFactory @Inject constructor(
    private val StartForegroundByFromBackWayUseCase: StartForegroundByFromBackWayUseCase,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            StartForegroundFromBackWorker::class.java.name ->
                StartForegroundFromBackWorker(
                    ctx = appContext,
                    params = workerParameters,
                    StartForegroundByFromBackWayUseCase = StartForegroundByFromBackWayUseCase,
                )

            else -> null
        }
    }
}
