package com.chrrissoft.services.foreground.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.services.Util.add
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.DataSync
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.Media
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.PhoneCall
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.ShortService
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.SpecialUse
import com.chrrissoft.services.foreground.entities.ServiceToStart.NormalTypeService.SystemExempted
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.Camera
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.Health
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.Location
import com.chrrissoft.services.foreground.entities.ServiceToStart.WhileInUseTypeService.Microphone
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.BatteryOptimizationDisabled
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.ExactAlarm
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.NormalTypes.OverAppPermission
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.Notification
import com.chrrissoft.services.foreground.entities.WayToStartFromBack.WhileInUseTypes
import com.chrrissoft.services.shared.SnackbarData

data class ForegroundServicesState(
    val page: Page = Page.NORMAL_TYPES,
    val snackbarData: SnackbarData = SnackbarData(),
    val normalTypes: ForegroundServicesNormalTypesState = ForegroundServicesNormalTypesState(),
    val whileInUseTypes: ForegroundServicesWhileInUseTypesState = ForegroundServicesWhileInUseTypesState(),
) {
    enum class Page(val label: String, val icon: ImageVector) {
        NORMAL_TYPES(label = "Types", icon = Icons.Rounded.FilterAlt),
        WHILE_IN_USE_TYPES(label = "While in use", icon = Icons.Rounded.FilterAlt),
        ;

        companion object {
            val pages = listOf(NORMAL_TYPES, WHILE_IN_USE_TYPES)
        }
    }

    data class ForegroundServicesNormalTypesState(
        val services: List<NormalTypeService> = buildList {
            add { DataSync() }
            add { Media() }
            add { PhoneCall() }
            add { ShortService() }
            add { SpecialUse() }
            add { SystemExempted() }
        },
        val wayToStartFromBack: List<NormalTypes> = buildList {
            add(ExactAlarm())
            add(OverAppPermission())
            add(BatteryOptimizationDisabled())
            add(Notification())
        },
    )

    data class ForegroundServicesWhileInUseTypesState(
        val services: List<WhileInUseTypeService> = buildList {
            add(Health())
            add(Camera())
            add(Location())
            add(Microphone())
        },
        val wayToStartFromBack: List<WhileInUseTypes> = buildList {
            add(Notification())
        },
    )
}
