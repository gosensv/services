package com.chrrissoft.services.started

import androidx.core.app.NotificationManagerCompat
import com.chrrissoft.services.ServicesApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object StartedServiceDependencyModule {
    @Provides
    fun provide(
        app: ServicesApp,
        scope: CoroutineScope,
        notificationManagerCompat: NotificationManagerCompat,
    ): StartedServiceDependency = StartedServiceDependencyImpl(
        app = app,
        scope = scope,
        notificationManagerCompat = notificationManagerCompat
    )
}
