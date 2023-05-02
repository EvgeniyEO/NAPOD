package com.minaev.apod.domain.repository

import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.domain.entity.DomainResource
import com.minaev.apod.domain.entity.errors.ErrorEntity
import kotlinx.coroutines.flow.Flow

interface INasaRepository {
    /**
     * Функция для получения Nasa Astronomy Picture of the Day
     */
    suspend fun getApod() : DomainResource<ApodEntity, ErrorEntity>

    /**
     * Получение APOD разных дат из локального репозитория
     */
    fun getApodList(count: Int): Flow<DomainResource<List<ApodEntity>, ErrorEntity>>

    /**
     * Получение случайных APOD разных дат в колличестве [count]
     * @param count колличество запрашиваемых APOD
     */
    suspend fun refreshApodList(count: Int): DomainResource<Unit, ErrorEntity>
}