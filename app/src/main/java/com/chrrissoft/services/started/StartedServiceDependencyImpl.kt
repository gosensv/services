package com.chrrissoft.services.started

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chrrissoft.services.Constants.GENERAL_NOTIFICATIONS_CHANNEL_ID
import com.chrrissoft.services.R
import com.chrrissoft.services.ServicesApp
import com.chrrissoft.services.Util.generateRandom
import com.chrrissoft.services.started.StartedServiceDependency.OnFinishedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@SuppressLint("MissingPermission")
class StartedServiceDependencyImpl @Inject constructor(
    private val app: ServicesApp,
    private val scope: CoroutineScope,
    private val notificationManagerCompat: NotificationManagerCompat,
) : StartedServiceDependency {
    private val works = ConcurrentHashMap<Int, Boolean>()
    private val mutex = Mutex()
    private val finishedListener = mutableListOf<OnFinishedListener>()

    override fun startWork(data: String, id: Int) {
        works[id] = false
        scope.launch {
            generateRandom().forEach { pair ->
                NotificationCompat.Builder(app, GENERAL_NOTIFICATIONS_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(data)
                    .setProgress((100), pair.first, (false))
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .build()
                    .let { notificationManagerCompat.notify(id, it) }
                delay(pair.second)
            }
            notificationManagerCompat.cancel(id)
            mutex.withLock {
                works[id] = true
                if (works.all { it.value }) finishedListener.forEach { it() }
            }
        }
    }

    override fun stopWork() {
        works.forEach {
            if (it.value) return@forEach
            notificationManagerCompat.cancel(it.key)
        }
        works.clear()
        finishedListener.clear()
        scope.cancel()
    }

    override fun addOnFinishedListener(listener: OnFinishedListener) {
        finishedListener.add(listener)
    }

    override fun removeOnFinishedListener(listener: OnFinishedListener) {
        finishedListener.remove(listener)
    }
}
