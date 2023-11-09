package com.chrrissoft.services.foreground.usecases.modules

import com.chrrissoft.services.foreground.usecases.classes.ResolveServiceEnableUseCaseImpl
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServiceEnableUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveServiceEnableUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveServiceEnableUseCaseImpl) : ResolveServiceEnableUseCase
}
