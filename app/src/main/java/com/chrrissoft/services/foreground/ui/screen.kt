package com.chrrissoft.services.foreground.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.services.Util.setBarsColors
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnChangePage
import com.chrrissoft.services.foreground.ui.ForegroundServicesState.Page.Companion.pages
import com.chrrissoft.services.foreground.ui.components.ScreenContent
import com.chrrissoft.services.shared.NotificationsSnackbar
import com.chrrissoft.services.ui.components.TopBarTitle
import com.chrrissoft.services.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.services.ui.theme.navigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForegroundServicesScreen(
    state: ForegroundServicesState,
    onEvent: (ForegroundServicesEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    setBarsColors()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) { Icon(Icons.Rounded.Menu, (null)) }
                },
                colors = centerAlignedTopAppBarColors,
                title = { TopBarTitle(title = "Foreground Services") },
            )
        },
        bottomBar = {
            NavigationBar(containerColor = colorScheme.primaryContainer) {
                pages.forEach {
                    NavigationBarItem(
                        selected = state.page == it,
                        colors = navigationBarItemColors,
                        icon = { Icon(it.icon, (null)) },
                        label = { Text(text = it.label) },
                        onClick = { onEvent(OnChangePage(it)) },
                    )
                }
            }
        },
        snackbarHost = {
            NotificationsSnackbar(data = state.snackbarData)
        },
        containerColor = colorScheme.onPrimary,
    ) {
        ScreenContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.padding(it)
        )
    }
}
