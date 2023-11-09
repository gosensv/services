package com.chrrissoft.services.foreground.usecases.interfaces

import com.chrrissoft.services.foreground.entities.WayToStartFromBack

interface ResolveWaysToStartFromBackUseCase {
    operator fun<T : WayToStartFromBack> invoke(ways: List<T>): List<T>
}
