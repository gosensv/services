package com.chrrissoft.services.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.services.ui.theme.ServicesTheme

@Composable
fun AppUi(app: @Composable () -> Unit) {
    ServicesTheme {
        Surface(
            color = colorScheme.onPrimary,
            modifier = Modifier.fillMaxSize()
        ) { app() }
    }
}
