package com.chrrissoft.services.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chrrissoft.services.ui.theme.checkboxColors

@Composable
fun TextCheck(
    text: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = checkboxColors,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .clickable(enabled) { onCheckedChange(!checked) },
    ) {
        val fontWeight = if (checked) FontWeight.Bold else FontWeight.Normal
        Text(
            text = text,
            style = typography.bodyLarge
                .copy(color = colorScheme.onPrimaryContainer, fontWeight = fontWeight)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Checkbox(
            colors = colors,
            enabled = enabled,
            checked = checked,
            onCheckedChange = onCheckedChange,
            interactionSource = interactionSource,
        )
    }
}
