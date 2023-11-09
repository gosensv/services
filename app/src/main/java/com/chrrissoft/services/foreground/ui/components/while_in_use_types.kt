package com.chrrissoft.services.foreground.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.*
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.ForegroundServicesWhileInUseTypesState
import com.chrrissoft.services.ui.components.Page

@Composable
fun ForegroundServicesWhileInUseTypesPage(
    state: ForegroundServicesWhileInUseTypesState,
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
