package com.chrrissoft.services.navigation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chrrissoft.services.R.drawable.service_background_image
import com.chrrissoft.services.R.drawable.service_foreground_image
import com.chrrissoft.services.bound.ui.BoundServiceScreen
import com.chrrissoft.services.bound.ui.BoundServiceViewModel
import com.chrrissoft.services.navigation.Screen.*
import com.chrrissoft.services.Util.open
import com.chrrissoft.services.Util.close
import com.chrrissoft.services.foreground.ui.ForegroundServicesEvent.OnForegroundAppRegistration
import com.chrrissoft.services.foreground.ui.ForegroundServicesScreen
import com.chrrissoft.services.foreground.ui.ForegroundServicesViewModel
import com.chrrissoft.services.navigation.Screen.Companion.screens
import com.chrrissoft.services.started.ui.StartedServiceScreen
import com.chrrissoft.services.started.ui.StartedServiceViewModel
import com.chrrissoft.services.ui.theme.navigationDrawerItemColors

@Composable
fun Graph() {
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = Closed)
    val scope = rememberCoroutineScope()
    val backStack by controller.currentBackStackEntryAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.primaryContainer
            ) {
                DrawerHeader()
                screens.forEach {
                    NavigationDrawerItem(
                        shape = shapes.medium,
                        icon = {
                            val color = if (backStack?.destination?.route==it.route)
                                colorScheme.onPrimary
                            else colorScheme.secondary.copy(.5f)
                            when (it) {
                                BACKGROUND -> {
                                    Image(
                                        contentDescription = null,
                                        painter = painterResource(id = service_background_image),
                                        colorFilter = ColorFilter.tint(color),
                                        modifier = Modifier.size(35.dp),
                                    )
                                }

                                FOREGROUND -> {
                                    Image(
                                        contentDescription = null,
                                        painter = painterResource(id = service_foreground_image),
                                        colorFilter = ColorFilter.tint(color),
                                        modifier = Modifier.size(35.dp),
                                    )
                                }

                                BOUND -> Icon(Icons.Rounded.Link, (null))
                            }
                        },
                        label = { Text(text = it.label) },
                        colors = navigationDrawerItemColors,
                        selected = backStack?.destination?.route == it.route,
                        onClick = { drawerState.close(scope); controller.navigate(it.route) },
                        modifier = Modifier.padding(horizontal = 10.dp),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        },
    ) {
        NavHost(controller, BACKGROUND.route) {
            composable(BACKGROUND.route) {
                val viewModel = hiltViewModel<StartedServiceViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                StartedServiceScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(FOREGROUND.route) {
                val viewModel = hiltViewModel<ForegroundServicesViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                val ctx = LocalContext.current as ComponentActivity
                LaunchedEffect(Unit) {
                    viewModel.handleEvent(OnForegroundAppRegistration((true), ctx))
                }

                DisposableEffect(Unit) {
                    onDispose {
                        viewModel.handleEvent(OnForegroundAppRegistration((false), ctx))
                    }
                }

                ForegroundServicesScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(BOUND.route) {
                val viewModel = hiltViewModel<BoundServiceViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                BoundServiceScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }
        }
    }
}
