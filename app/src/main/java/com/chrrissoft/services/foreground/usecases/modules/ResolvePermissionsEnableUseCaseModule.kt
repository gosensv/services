package com.chrrissoft.services.foreground.usecases.modules

import com.chrrissoft.services.foreground.usecases.classes.ResolvePermissionsEnableUseCaseImpl
import com.chrrissoft.services.foreground.usecases.interfaces.ResolvePermissionsEnableUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolvePermissionsEnableUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolvePermissionsEnableUseCaseImpl) : ResolvePermissionsEnableUseCase
}
