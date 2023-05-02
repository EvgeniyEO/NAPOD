package com.minaev.apod.data.repository.datasource

import com.minaev.apod.BuildConfig
import com.minaev.apod.data.remote.service.INasaApiService
import javax.inject.Inject

class NasaRemoteDataSourceFactory @Inject constructor(
    private val nasaApiService: INasaApiService
) {
    fun createApodDataSource(): INasaRemoteDataSource {
        if (BuildConfig.DATA_SOURCE == "NETWORK"){
            return NasaRemoteDataSource(nasaApiService)
        } else {
            return NasaTestRemoteDataSource()
        }
    }
}