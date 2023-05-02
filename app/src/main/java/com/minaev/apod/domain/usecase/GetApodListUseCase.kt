package com.minaev.apod.domain.usecase

import com.minaev.apod.domain.repository.INasaRepository
import javax.inject.Inject

class GetApodListUseCase @Inject constructor(
    private val repository: INasaRepository
){
    operator fun invoke(count: Int) = repository.getApodList(count)
}