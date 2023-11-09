package com.chrrissoft.services.ui.theme

import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBarItemDefaults.colors as navigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
val centerAlignedTopAppBarColors
    @Composable get() = centerAlignedTopAppBarColors(
        containerColor = colorScheme.primaryContainer,
        navigationIconContentColor = colorScheme.primary,
        titleContentColor = colorScheme.primary,
        actionIconContentColor = colorScheme.primary,
    )

val checkboxColors
    @Composable get() = CheckboxDefaults.colors(
        checkedColor = colorScheme.onPrimaryContainer,
        uncheckedColor = colorScheme.onPrimaryContainer,
        checkmarkColor = colorScheme.onPrimary,
        disabledCheckedColor = colorScheme.error,
        disabledUncheckedColor = colorScheme.error,
        disabledIndeterminateColor = colorScheme.error,
    )

val navigationBarItemColors
    @Composable get() = navigationBarItemColors(
        selectedIconColor = colorScheme.onPrimary,
        selectedTextColor = colorScheme.primary,
        indicatorColor = colorScheme.primary,
        unselectedIconColor = colorScheme.primary.copy(.5f),
        unselectedTextColor = colorScheme.primary.copy(.5f),
    )

val navigationDrawerItemColors
    @Composable get() = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = colorScheme.primary,
        unselectedContainerColor = colorScheme.onPrimary,
        selectedIconColor = colorScheme.onPrimary,
        unselectedIconColor = colorScheme.secondary.copy(.5f),
        selectedTextColor = colorScheme.onPrimary,
        unselectedTextColor = colorScheme.secondary.copy(.5f),
    )

@OptIn(ExperimentalMaterial3Api::class)
val inputChipColors
    @Composable get() = inputChipColors(
        containerColor = colorScheme.onPrimary,
        labelColor = colorScheme.primary,
        leadingIconColor = colorScheme.primary,
        trailingIconColor = colorScheme.primary,
        disabledContainerColor = colorScheme.primaryContainer.copy(.5f),
        disabledLabelColor = colorScheme.secondary.copy(.5f),
        disabledLeadingIconColor = colorScheme.secondary.copy(.5f),
        disabledTrailingIconColor = colorScheme.secondary.copy(.5f),
        selectedContainerColor = colorScheme.primary,
        disabledSelectedContainerColor = colorScheme.primary.copy(.5f),
        selectedLabelColor = colorScheme.onPrimary,
        selectedLeadingIconColor = colorScheme.onPrimary,
        selectedTrailingIconColor = colorScheme.onPrimary,
    )

val cardColors
    @Composable get() = CardDefaults.cardColors(
        containerColor = colorScheme.primaryContainer,
    )

val textFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedTextColor = colorScheme.onPrimaryContainer,
        unfocusedTextColor = colorScheme.onPrimaryContainer,
        focusedContainerColor = colorScheme.onPrimary,
        unfocusedContainerColor = colorScheme.onPrimary,
        cursorColor = colorScheme.onPrimaryContainer,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedLabelColor = colorScheme.onPrimaryContainer,
        focusedLabelColor = colorScheme.onPrimaryContainer,
        disabledContainerColor = colorScheme.primaryContainer,
        disabledIndicatorColor = Color.Transparent,
        disabledTextColor = colorScheme.onPrimaryContainer,
    )
