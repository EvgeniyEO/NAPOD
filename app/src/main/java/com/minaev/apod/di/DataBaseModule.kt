package com.minaev.apod.di

import android.content.Context
import com.minaev.apod.data.local.db.AppDataBase
import com.minaev.apod.data.local.db.daos.NasaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.provideAppDatabase(context)
    }

    @Singleton
    @Provides
    fun provideNasaDao(appDataBase: AppDataBase): NasaDao {
        return appDataBase.nasaDao()
    }
}
