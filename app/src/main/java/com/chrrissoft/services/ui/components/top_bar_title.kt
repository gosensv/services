package com.chrrissoft.services.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight.Companion.Bold

@Composable
fun TopBarTitle(title: String) {
    Text(text = title, fontWeight = Bold)
}
