package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.WayToStartFromBack

interface ResolveWayToStartFromBackUseCase {
    operator fun<T : WayToStartFromBack> invoke(way: T): T
}
