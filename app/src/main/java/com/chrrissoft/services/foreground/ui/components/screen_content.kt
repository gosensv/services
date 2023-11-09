package com.chrrissoft.services.foreground.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent
import com.chrrissoft.services.foreground.ui.ForegroundServicesState
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.Page.NORMAL_TYPES
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.Page.WHILE_IN_USE_TYPES

@Composable
fun ScreenContent(
    state: ForegroundServicesState,
    onEvent: (ForegroundServicesEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.page) {
        NORMAL_TYPES -> ForegroundServicesTypesPage(
            state = state.normalTypes,
            onEvent = onEvent,
            modifier = modifier,
        )
        WHILE_IN_USE_TYPES -> ForegroundServicesWhileInUseTypesPage(
            state = state.whileInUseTypes,
            onEvent = onEvent,
            modifier = modifier,
        )
    }
}
