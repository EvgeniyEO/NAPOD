package com.minaev.apod.data.repository.datasource

import com.minaev.apod.data.remote.model.ApodResponse

interface INasaRemoteDataSource {
    suspend fun getApodToday() : ApodResponse
    suspend fun getApodList(count: Int) : List<ApodResponse>
}