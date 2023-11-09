package com.chrrissoft.services.bound.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.services.bound.BoundBinderService.State
import com.chrrissoft.services.shared.ResState
import com.chrrissoft.services.shared.ResState.Success
import com.chrrissoft.services.shared.SnackbarData

data class BoundServiceState(
    val page: Page = Page.BINDER,
    val binder: BinderState = BinderState(),
    val snackbarData: SnackbarData = SnackbarData(),
) {
    enum class Page(val icon: ImageVector, val label: String) {
        BINDER(Icons.Rounded.Link, label = "Binder"),
        MESSAGES(Icons.Rounded.Mail, label = "Messages"),
        AIDL(Icons.Rounded.Description, label = "Aidl"),
        ;

        companion object {
            val pages = listOf(BINDER, MESSAGES, AIDL)
        }
    }

    data class BinderState(
        val state: State = State(),
        val appBound: ResState<Boolean> = Success((false)),
        val serviceBound: ResState<Boolean> = Success((false)),
        val activityBound: ResState<Boolean> = Success((false)),
    )
}
