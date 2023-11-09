package com.chrrissoft.services.foreground.usecases.modules

import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWayToStartFromBackUseCase
import com.chrrissoft.services.foreground.usecases.classes.ResolveWayToStartFromBackUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveWayToStartFromBackUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveWayToStartFromBackUseCaseImpl) : ResolveWayToStartFromBackUseCase
}
