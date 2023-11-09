package com.chrrissoft.services.foreground.ui.components

import android.Manifest.permission.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.S
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.chrrissoft.services.foreground.entities.Permission
import com.chrrissoft.services.foreground.entities.Permission.*
import com.chrrissoft.services.foreground.ui.Util.label
import com.chrrissoft.services.ui.theme.inputChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicePermissionItem(
    state: Permission,
    onRequestResult: (Boolean) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(contract = RequestPermission()) {
        onRequestResult(it)
    }

    InputChip(
        selected = state.enabled,
        colors = inputChipColors,
        label = { Text(state.label) },
        border = InputChipDefaults.inputChipBorder(Color.Transparent),
        enabled = state.available && !state.enabled,
        onClick = {
            when (state) {
                is Camera -> launcher.launch(CAMERA)
                is AccessCoarseLocation -> launcher.launch(ACCESS_COARSE_LOCATION)
                is AccessFineLocation -> launcher.launch(ACCESS_FINE_LOCATION)
                is ActivityRecognition -> if (SDK_INT >= Q) launcher.launch(ACTIVITY_RECOGNITION)
                is BodySensors -> launcher.launch(BODY_SENSORS)
                is HighSamplingRateSensors -> if (SDK_INT >= S) launcher.launch(HIGH_SAMPLING_RATE_SENSORS)
                is ManageOwnCalls -> if (SDK_INT >= O) launcher.launch(MANAGE_OWN_CALLS)
                is RecordAudio -> launcher.launch(RECORD_AUDIO)
                is ScheduleExactAlarm -> if (SDK_INT >= S) launcher.launch(SCHEDULE_EXACT_ALARM)
                is UseExactAlarm -> if (SDK_INT >= TIRAMISU) launcher.launch(USE_EXACT_ALARM)
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}
