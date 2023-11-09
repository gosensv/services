package com.chrrissoft.services.di

import android.content.Context
import com.chrrissoft.services.ServicesApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provide(@ApplicationContext ctx: Context): ServicesApp = ctx as ServicesApp
}
