package com.chrrissoft.services.foreground.usecases.modules

import com.chrrissoft.services.foreground.usecases.classes.ResolveServicesEnableUseCaseImpl
import com.chrrissoft.services.foreground.usecases.interfaces.ResolveServicesEnableUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveServicesEnableUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveServicesEnableUseCaseImpl) : ResolveServicesEnableUseCase
}
