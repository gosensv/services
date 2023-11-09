package com.chrrissoft.services.bound.di

import com.chrrissoft.services.shared.ResState
import com.chrrissoft.services.shared.ResState.Success
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Bounds {
    @Provides
    @Singleton
    @App
    fun provideReceiver(): MutableStateFlow<ResState<Boolean>> = MutableStateFlow(Success((false)))

    @Provides
    @Singleton
    @Service
    fun provideService(): MutableStateFlow<ResState<Boolean>> = MutableStateFlow(Success((false)))


    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class App

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Service
}
