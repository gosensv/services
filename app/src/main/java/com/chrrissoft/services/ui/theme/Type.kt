package com.chrrissoft.services.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

val Typography @Composable get() = Typography(
    displayLarge = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
    displayMedium = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Medium),
    displaySmall = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Medium),
    headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
    headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
    headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
    titleLarge = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
    titleMedium = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
    titleSmall = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
    bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
    bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
    bodySmall = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
    labelLarge = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
    labelMedium = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
    labelSmall = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
)