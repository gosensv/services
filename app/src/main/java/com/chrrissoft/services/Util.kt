package com.chrrissoft.services

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.getForegroundService
import android.app.PendingIntent.getService
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.chrrissoft.services.Constants.ACTION_START_FOREGROUND_SERVICE
import com.chrrissoft.services.Constants.ACTION_STOP_FOREGROUND_SERVICE
import com.chrrissoft.services.Constants.EXTRA_STOP_SERVICE_AFTER_WORK
import com.chrrissoft.services.Constants.GENERAL_NOTIFICATIONS_CHANNEL_ID
import com.chrrissoft.services.R.mipmap.ic_launcher
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

object Util {
    @SuppressLint("ComposableNaming")
    @Composable
    fun setBarsColors(
        status: Color = colorScheme.primaryContainer,
        bottom: Color = colorScheme.primaryContainer,
    ) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        DisposableEffect(systemUiController, useDarkIcons) {
            systemUiController.setStatusBarColor(status, useDarkIcons)
            systemUiController.setNavigationBarColor(bottom)
            onDispose {}
        }
    }

    fun DrawerState.open(scope: CoroutineScope) {
        scope.launch { open() }
    }

    fun DrawerState.close(scope: CoroutineScope) {
        scope.launch { close() }
    }

    fun Intent.putData(data: String): Intent = putExtra("data", data)

    val Intent.getData get(): String = getStringExtra("data") ?: "null"

    val Intent.actionStopForeground get() = action == ACTION_STOP_FOREGROUND_SERVICE
    val Intent.actionStartForeground get() = action == ACTION_START_FOREGROUND_SERVICE
    val Intent.extraStopAfterWork get() = getBooleanExtra(EXTRA_STOP_SERVICE_AFTER_WORK, (false))

    fun generalNotification(
        ctx: Context,
        title: String,
        channel: String = GENERAL_NOTIFICATIONS_CHANNEL_ID,
        icon: Int = ic_launcher,
        text: String? = null,
        subText: String? = null,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(ctx, channel)
            .setContentTitle(title)
            .setOnlyAlertOnce(true)
            .setSmallIcon(icon)
            .setContentText(text)
            .setSubText(subText)
    }

    fun Context.hasPermission(permission: String): Boolean {
        return checkSelfPermission(permission) == PERMISSION_GRANTED
    }

    fun AlarmManager.canScheduleExactAlarmsCompact(): Boolean {
        return if (SDK_INT >= S) canScheduleExactAlarms()
        else true
    }

    fun Context.startActivity(intent: Intent, onException: (Throwable) -> Unit) {
        try {
            startActivity(intent)
        } catch (e: Throwable) {
            onException(e)
        }
    }

    fun Context.startForegroundServiceCompat(intent: Intent): ComponentName? {
        return if (SDK_INT >= O) startForegroundService(intent)
        else startService(intent)
    }

    fun Any.debug(message: Any?, tag: String = this::class.java.simpleName) {
        Log.d(tag, message.toString())
    }

    fun generateRandom(): List<Pair<Int, Long>> {
        return generateRandomProgress().map {
            it to Random.nextLong(1000, 3000)
        }
    }

    private fun generateRandomProgress(): List<Int> {
        val random = mutableListOf(1)
        while ((random.last() < 100)) {
            val last = random.last()
            var new = Random.nextInt(last, (last + 20))
            if (new > 100) new = 100
            random.add(new)
        }
        return random
    }

    fun <T> MutableList<T>.add(index: Int = lastIndex + 1, block: MutableList<T>.() -> T) {
        add(index, block())
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun Context.registerNotExportedReceiver(receiver: BroadcastReceiver, filter: IntentFilter) {
        if (SDK_INT >= TIRAMISU) registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        else registerReceiver(receiver, filter)
    }

    fun getForegroundServiceCompat(
        ctx: Context, requestCode: Int, intent: Intent, flag: Int
    ): PendingIntent {
        return if (SDK_INT >= O) getForegroundService(ctx, requestCode, intent, flag)
        else getService(ctx, requestCode, intent, flag)
    }

    inline fun<reified T : Parcelable> Intent.getParcelableCompat(key: String): T? {
        return if (SDK_INT >= TIRAMISU) getParcelableExtra(key, T::class.java)
        else getParcelableExtra(key)
    }
}
