package com.chrrissoft.services

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chrrissoft.services.Util.setBarsColors
import com.chrrissoft.services.foreground.ui.ForegroundServicesViewModel.Companion.ACTION_ON_APP_FOREGROUND
import com.chrrissoft.services.navigation.Graph
import com.chrrissoft.services.ui.components.AppUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUi {
                setBarsColors()
                Graph()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sendBroadcast(Intent(ACTION_ON_APP_FOREGROUND))
    }
}
