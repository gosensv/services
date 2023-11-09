package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.ServiceToStart
import com.chrrissoft.services.foreground.entities.WayToStartFromBack

interface StartForegroundByFromBackWayUseCase {
    operator fun invoke(service: ServiceToStart, way: WayToStartFromBack): Boolean
}
