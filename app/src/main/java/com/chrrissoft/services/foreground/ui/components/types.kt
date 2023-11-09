package com.chrrissoft.services.foreground.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnServicesPermissionAsk
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnWaysToStartFromBackAsk
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStart
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStartFromBack
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnStop
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.ForegroundServicesNormalTypesState
import com.chrrissoft.services.ui.components.Page

@Composable
fun ForegroundServicesTypesPage(
    state: ForegroundServicesNormalTypesState,
    onEvent: (ForegroundServicesEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) { onEvent(OnWaysToStartFromBackAsk) }
    LaunchedEffect(Unit) { onEvent(OnServicesPermissionAsk) }
    Page(modifier) {
        ServiceList(
            state = state.services,
            ways = state.wayToStartFromBack,
            onStop = { onEvent(OnStop(it)) },
            onStart = { onEvent(OnStart(it)) },
            onStartInBack = { way, service -> onEvent(OnStartFromBack(way, service)) },
            onRequestResult = { onEvent(OnServicesPermissionAsk) },
            onWayRequestResult = { onEvent(OnWaysToStartFromBackAsk) }
        )
    }
}
