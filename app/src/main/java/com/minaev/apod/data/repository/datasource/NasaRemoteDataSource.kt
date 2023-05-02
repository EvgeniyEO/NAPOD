package com.minaev.apod.data.repository.datasource

import com.minaev.apod.BuildConfig
import com.minaev.apod.data.remote.model.ApodResponse
import com.minaev.apod.data.remote.service.INasaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NasaRemoteDataSource constructor(
    private val nasaApiService: INasaApiService
): INasaRemoteDataSource {

    override suspend fun getApodToday() : ApodResponse{
        return withContext(Dispatchers.IO){
            nasaApiService.getApodToday(BuildConfig.NASA_APOD_API_KEY)
        }
    }

    override suspend fun getApodList(count: Int): List<ApodResponse> {
        return withContext(Dispatchers.IO){
            nasaApiService.getApodList(BuildConfig.NASA_APOD_API_KEY, count)
        }
    }

}