package com.chrrissoft.services.di

import android.app.AlarmManager
import com.chrrissoft.services.ServicesApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmManagerModule {
    @Provides
    fun provide(app: ServicesApp): AlarmManager = app.getSystemService(AlarmManager::class.java)
}
