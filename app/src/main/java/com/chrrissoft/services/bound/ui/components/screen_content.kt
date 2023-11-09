package com.chrrissoft.services.bound.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import com.chrrissoft.services.bound.ui.BoundServiceEvent
import com.chrrissoft.services.bound.ui.BoundServiceState
import com.chrrissoft.services.bound.ui.BoundServiceState.Page.AIDL
import com.chrrissoft.services.bound.ui.BoundServiceState.Page.BINDER
import com.chrrissoft.services.bound.ui.BoundServiceState.Page.MESSAGES

@Composable
fun ScreenContent(
    state: BoundServiceState,
    onEvent: (BoundServiceEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.page) {
        BINDER -> BinderServicePage(state.binder, onEvent, modifier)
        MESSAGES -> {
            Box(Modifier.fillMaxSize(), Center) {
                Text(text = "Messages not developed yet ☹️", color = colorScheme.primary)
            }
        }
        AIDL -> {
            Box(Modifier.fillMaxSize(), Center) {
                Text(text = "AIDL not developed yet ☹️", color = colorScheme.primary)
            }
        }
    }
}
