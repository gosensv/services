package com.chrrissoft.services.foreground.ui.components

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import com.chrrissoft.services.Util.startActivity
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.*
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification
import com.chrrissoft.services.foreground.ui.Util.label
import com.chrrissoft.services.ui.components.Card
import com.chrrissoft.services.ui.theme.inputChipColors

@Composable
fun <T : WayToStartFromBack> PermissionToStartFromBackUi(
    state: List<T>,
    onStart: (T) -> Unit,
    onRequestResult: () -> Unit,
) {
    Card(title = "Ways to start from back") {
        LazyColumn {
            items(state) {
                WayToStartFromBackUi(
                    state = it,
                    onStart = { onStart(it) },
                    onRequestResult = onRequestResult,
                )
            }
        }
    }
}

@SuppressLint("BatteryLife")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WayToStartFromBackUi(
    onStart: () -> Unit,
    state: WayToStartFromBack,
    onRequestResult: () -> Unit,
) {
    LaunchedEffect(Unit) {
        onRequestResult()
    }

    val launcher = rememberLauncherForActivityResult(contract = RequestMultiplePermissions()) {
        onRequestResult()
    }
    val ctx = LocalContext.current

    InputChip(
        selected = state.enabled,
        colors = inputChipColors,
        onClick = {
            if (state.enabled) {
                onStart()
                return@InputChip
            }
            when (state) {
                is ExactAlarm -> {
                    val list = buildList {
                        if (SDK_INT >= TIRAMISU) add(USE_EXACT_ALARM)
                        if (SDK_INT >= S) add(SCHEDULE_EXACT_ALARM)
                    }
                    if (list.isEmpty()) return@InputChip
                    launcher.launch(list.toTypedArray())
                }

                is Notification -> {
                    if (SDK_INT < TIRAMISU) return@InputChip
                    launcher.launch(listOf(POST_NOTIFICATIONS).toTypedArray())
                }

                is OverAppPermission -> {
                    Intent(ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                        data = Uri.parse(("package:${ctx.packageName}"))
                        ctx.startActivity(intent = this) {
                            Intent(ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                                ctx.startActivity(intent = this) {}
                            }
                        }
                    }
                }

                is BatteryOptimizationDisabled -> {
                    Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse(("package:${ctx.packageName}"))
                        ctx.startActivity(intent = this) {
                            Intent(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                                ctx.startActivity(this) { }
                            }
                        }
                    }
                }
            }
        },
        border = inputChipBorder(borderColor = Transparent),
        label = { Text(style = typography.labelSmall, text = state.label) },
        modifier = Modifier.fillMaxWidth()
    )
}
