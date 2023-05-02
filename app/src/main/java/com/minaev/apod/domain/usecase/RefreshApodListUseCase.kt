package com.minaev.apod.domain.usecase

import com.minaev.apod.domain.repository.INasaRepository
import javax.inject.Inject

class RefreshApodListUseCase @Inject constructor(
    private val repository: INasaRepository
){
    suspend operator fun invoke(count: Int) = repository.refreshApodList(count)
}