package com.chrrissoft.services.foreground.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.WayToStartFromBack

@Composable
fun<W : WayToStartFromBack, T: ServiceToStart> ServiceList(
    state: List<T>,
    ways: List<W>,
    onStop: (T) -> Unit,
    onStart: (T) -> Unit,
    onStartInBack: (W, T) -> Unit,
    onRequestResult: () -> Unit,
    onWayRequestResult: () -> Unit,
) {
    LazyColumn {
        items(state) { service ->
            ForegroundServiceItem(
                ways = ways,
                service = service,
                onStop = { onStop(service) },
                onStart = { onStart(service) },
                onStartInBack = { onStartInBack(it, service) },
                onRequestResult = onRequestResult,
                onWayRequestResult = onWayRequestResult
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
