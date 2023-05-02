package com.minaev.apod.domain.usecase

import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.domain.entity.DomainResource
import com.minaev.apod.domain.entity.errors.ErrorEntity
import com.minaev.apod.domain.repository.INasaRepository
import javax.inject.Inject

class GetApodUseCase @Inject constructor(
    private val repository: INasaRepository
){
    suspend operator fun invoke(): DomainResource<ApodEntity, ErrorEntity> = repository.getApod()
}