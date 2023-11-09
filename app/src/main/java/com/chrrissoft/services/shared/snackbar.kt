package com.chrrissoft.services.shared

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NotificationsSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState = data.state, modifier = modifier) {
        Snackbar(
            snackbarData = it,
            actionColor = data.type.actionColor,
            contentColor = data.type.contentColor,
            containerColor = data.type.containerColor,
            actionContentColor = data.type.actionContentColor,
            dismissActionContentColor = data.type.dismissActionContentColor,
        )
    }
}
