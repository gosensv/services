package com.chrrissoft.services.di

import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.from
import com.chrrissoft.services.ServicesApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationManagerCompatModule {
    @Provides
    fun provide(app: ServicesApp): NotificationManagerCompat = from(app)
}
