package com.chrrissoft.services.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.R.drawable.service_background_image

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth()
            .height(170.dp),
        contentAlignment = Alignment.Center
    ) { Image(painterResource(service_background_image), (null)) }
    Text(
        text = "Services App",
        fontWeight = Medium,
        textAlign = Center,
        style = typography.headlineLarge.copy(color = colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(10.dp))
}
