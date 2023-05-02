package com.minaev.apod.di

import com.minaev.apod.data.repository.NasaRepository
import com.minaev.apod.data.repository.datasource.INasaRemoteDataSource
import com.minaev.apod.data.repository.datasource.NasaRemoteDataSourceFactory
import com.minaev.apod.domain.repository.INasaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideNasaRemoteDataSourceFactory(nasaRemoteDataSourceFactory: NasaRemoteDataSourceFactory): INasaRemoteDataSource {
        return nasaRemoteDataSourceFactory.createApodDataSource()
    }


    @Provides
    fun provideNasaRepository(nasaRepository: NasaRepository): INasaRepository {
        return nasaRepository
    }
}