package com.chrrissoft.services.bound.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind.Ctx.Activity
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind.Ctx.App
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnBind.Ctx.Service
import com.chrrissoft.services.bound.ui.BoundServiceEvent.BinderServiceEvent.OnChangeState
import com.chrrissoft.services.bound.ui.BoundServiceState.BinderState
import com.chrrissoft.services.shared.ResState.Success
import com.chrrissoft.services.ui.components.Card
import com.chrrissoft.services.ui.components.MyTextField
import com.chrrissoft.services.ui.components.Page
import com.chrrissoft.services.ui.theme.inputChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinderServicePage(
    state: BinderState,
    onEvent: (BinderServiceEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Page(modifier) {
        Card(title = "Bind components") {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = SpaceBetween) {
                val activity = LocalContext.current as ComponentActivity
                InputChip(
                    colors = inputChipColors,
                    enabled = state.activityBound is Success,
                    border = inputChipBorder(Transparent),
                    selected = (state.activityBound as? Success)?.data == true,
                    modifier = Modifier.weight(1f),
                    label = { Text(text = if (state.activityBound is Success) "Activity" else "...") },
                    onClick = {
                        onEvent(OnBind(Activity(activity), (state.activityBound as? Success)?.data == false, activity))
                    },
                )
                Spacer(modifier = Modifier.weight(.05f))
                InputChip(
                    enabled = state.serviceBound is Success,
                    colors = inputChipColors,
                    border = inputChipBorder(Transparent),
                    label = { Text(text = if (state.serviceBound is Success) "Service" else "...") },
                    modifier = Modifier.weight(1f),
                    selected = (state.serviceBound as? Success)?.data == true,
                    onClick = {
                        onEvent(OnBind(Service, (state.serviceBound as? Success)?.data == false, activity))
                    },
                )
                Spacer(modifier = Modifier.weight(.05f))
                InputChip(
                    enabled = state.appBound is Success,
                    colors = inputChipColors,
                    border = inputChipBorder(Transparent),
                    label = { Text(text = if (state.appBound is Success) "App" else "...") },
                    modifier = Modifier.weight(1f),
                    selected = (state.appBound as? Success)?.data == true,
                    onClick = {
                        onEvent(OnBind(App, (state.appBound as? Success)?.data == false, activity))
                    },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            MyTextField(
                value = state.state.data,
                label = { Text(text = "Data") },
                onValueChange = { onEvent(OnChangeState(it)) },
                enabled = (state.activityBound as? Success)?.data == true,
            )
        }
    }
}
