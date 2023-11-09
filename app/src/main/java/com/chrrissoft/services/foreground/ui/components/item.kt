package com.chrrissoft.services.foreground.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.DoorBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.StopCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.ui.Util.label
import com.chrrissoft.services.ui.components.MyModalBottomSheet
import com.chrrissoft.services.ui.components.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <W : WayToStartFromBack> ForegroundServiceItem(
    service: ServiceToStart,
    ways: List<W>,
    onStop: () -> Unit,
    onStart: () -> Unit,
    onStartInBack: (W) -> Unit,
    onRequestResult: () -> Unit,
    onWayRequestResult: () -> Unit,
) {
    MyTextField(
        enabled = false,
        value = service.label,
        trailingIcon = {
            Row {
                FilledIconButton(
                    shape = shapes.medium,
                    onClick = { onStop() },
                    content = { Icon(Rounded.StopCircle, (null)) },
                )
                Spacer(modifier = Modifier.width(5.dp))
                FilledIconButton(
                    shape = shapes.medium,
                    onClick = { onStart() },
                    enabled = service.enabled,
                    content = {
                        Icon(
                            contentDescription = (null),
                            imageVector = Rounded.PlayCircle,
//                            modifier = Modifier.clickable(!service.enabled) { onStart() }
                        )
                    },
                )
                Spacer(modifier = Modifier.width(5.dp))


                val (showWays, changeShowWays) = remember {
                    mutableStateOf(value = false)
                }
                if (showWays) {
                    MyModalBottomSheet(
                        onDismissRequest = { changeShowWays(false) },
                        content = {
                            PermissionToStartFromBackUi(
                                state = ways,
                                onStart = {
                                    onStartInBack(it)
                                    changeShowWays(false)
                                },
                                onRequestResult = onWayRequestResult,
                            )
                        },
                    )
                }
                FilledIconButton(
                    shape = shapes.medium,
                    onClick = { changeShowWays(!showWays) },
                    content = { Icon(Rounded.DoorBack, (null)) },
                )
                Spacer(modifier = Modifier.width(5.dp))


                val (showPermission, changeShowPermission) = remember {
                    mutableStateOf(value = false)
                }
                if (showPermission) {
                    MyModalBottomSheet(
                        content = {
                            LazyColumn {
                                items(service.permissions) {
                                    ServicePermissionItem(
                                        state = it,
                                        onRequestResult = { onRequestResult() }
                                    )
                                }
                            }
                        },
                        title = "Permissions to start",
                        onDismissRequest = { changeShowPermission(false) },
                    )
                }

                FilledIconButton(
                    shape = shapes.medium,
                    onClick = { changeShowPermission(!showPermission) },
                    content = { Icon(Rounded.Lock, (null)) },
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        },
        onValueChange = {},
    )
}
