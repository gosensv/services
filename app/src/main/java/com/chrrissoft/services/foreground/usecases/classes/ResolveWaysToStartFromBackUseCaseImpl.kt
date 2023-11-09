package com.chrrissoft.services.foreground.usecases.classes

import com.chrrissoft.services.foreground.entities.WayToStartFromBack
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWayToStartFromBackUseCase
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWaysToStartFromBackUseCase
import javax.inject.Inject

class ResolveWaysToStartFromBackUseCaseImpl @Inject constructor(
    private val ResolveWayToStartFromBackUseCase: ResolveWayToStartFromBackUseCase,
) : ResolveWaysToStartFromBackUseCase {
    override fun <T : WayToStartFromBack> invoke(ways: List<T>): List<T> {
        return ways.map(ResolveWayToStartFromBackUseCase::invoke)
    }
}
