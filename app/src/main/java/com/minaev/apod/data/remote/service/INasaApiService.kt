package com.minaev.apod.data.remote.service

import com.minaev.apod.data.remote.model.ApodResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface INasaApiService {

    @GET("planetary/apod")
    suspend fun getApodToday(@Query("api_key")api_key:String) : ApodResponse

    @GET("planetary/apod")
    suspend fun getApodList(@Query("api_key")api_key:String, @Query("count") count:Int) : List<ApodResponse>


    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"

        fun create(okHttpClient: OkHttpClient): INasaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(INasaApiService::class.java)
        }
    }
}