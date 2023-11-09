package com.chrrissoft.services.foreground

import android.app.Service
import android.content.Intent
import com.chrrissoft.services.Util.getParcelableCompat

class StopForegroundService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        stopService(Intent().apply { component = intent.getParcelableCompat(EXTRA_SERVICE) })
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?) = null

    companion object {
        const val EXTRA_SERVICE = "com.chrrissoft.services.foreground.EXTRA_SERVICE"
    }
}
