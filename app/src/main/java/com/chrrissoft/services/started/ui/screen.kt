package com.chrrissoft.services.started.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.Util.setBarsColors
import com.chrrissoft.services.started.ui.StartedServiceEvent.*
import com.chrrissoft.services.ui.components.MyTextField
import com.chrrissoft.services.ui.components.TextCheck
import com.chrrissoft.services.ui.components.TopBarTitle
import com.chrrissoft.services.ui.theme.cardColors
import com.chrrissoft.services.ui.theme.centerAlignedTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartedServiceScreen(
    state: StartedServiceState,
    onEvent: (StartedServiceEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    setBarsColors(bottom = colorScheme.onPrimary)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) { Icon(Rounded.Menu, (null)) }
                },
                colors = centerAlignedTopAppBarColors,
                title = { TopBarTitle(title = "Started Services") },
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(10.dp)
            ) {
                Card(colors = cardColors) {
                    Column(Modifier.padding(12.dp)) {
                        MyTextField(
                            value = state.data,
                            label = { Text(text = "Data") },
                            trailingIcon = {
                                FilledIconButton(
                                    content = { Icon(AutoMirrored.Rounded.Send, (null)) },
                                    shape = shapes.medium,
                                    onClick = {
                                        onEvent(OnStartCommand(state.data, state.stopAtLastStart))
                                    },
                                    enabled = state.data.isNotEmpty(),
                                    modifier = Modifier.padding(end = 10.dp),
                                )
                            },
                            keyboardOptions = remember {
                                KeyboardOptions(capitalization = Sentences, imeAction = Send)
                            },
                            onValueChange = { onEvent(OnChangeState(state.copy(data = it))) },
                            keyboardActions = KeyboardActions {
                                if (state.data.isEmpty()) return@KeyboardActions
                                onEvent(OnStartCommand(state.data, state.stopAtLastStart))
                            }
                        )
                        TextCheck(
                            text = "Stop at the last start",
                            checked = state.stopAtLastStart,
                            onCheckedChange = { onEvent(OnChangeState(state.copy(stopAtLastStart = it))) },
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    shape = shapes.medium,
                    content = { Text(text = "Stop") },
                    onClick = { onEvent(OnStop) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    shape = shapes.medium,
                    content = { Text(text = "Stop foreground") },
                    onClick = { onEvent(OnStopForeground) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    shape = shapes.medium,
                    content = { Text(text = "Start foreground") },
                    onClick = { onEvent(OnStartForeground) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        containerColor = colorScheme.onPrimary
    )
}
