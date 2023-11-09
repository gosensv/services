package com.chrrissoft.services.di

import android.content.Context
import android.os.PowerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PowerManagerModule {
    @Provides
    fun provide(@ApplicationContext app: Context): PowerManager = app.getSystemService(PowerManager::class.java)
}
