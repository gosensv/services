package com.chrrissoft.services.foreground.usecases.modules

import com.chrrissoft.services.foreground.usecases.classes.ResolveWaysToStartFromBackUseCaseImpl
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveWaysToStartFromBackUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveWaysToStartFromBackUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveWaysToStartFromBackUseCaseImpl) : ResolveWaysToStartFromBackUseCase
}
