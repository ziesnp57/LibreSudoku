package com.kaajjo.libresudoku.di

import android.content.Context
import com.kaajjo.libresudoku.data.datastore.AppSettingsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppSettingsModule {

    @Provides
    @Singleton
    fun provideAppSettingsManager(
        @ApplicationContext context: Context
    ): AppSettingsManager {
        return AppSettingsManager(context)
    }
}