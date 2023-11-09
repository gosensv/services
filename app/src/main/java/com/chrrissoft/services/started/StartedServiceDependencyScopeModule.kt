package com.chrrissoft.services.started

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object StartedServiceDependencyScopeModule {
    @Provides
    fun provide(): CoroutineScope = CoroutineScope(SupervisorJob())
}
