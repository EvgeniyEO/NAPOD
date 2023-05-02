package com.minaev.apod.data.repository

import com.minaev.apod.data.mapper.toApodDto
import com.minaev.apod.data.mapper.toDomain
import com.minaev.apod.data.mapper.toApodListDto
import com.minaev.apod.data.repository.datasource.INasaRemoteDataSource
import com.minaev.apod.data.repository.datasource.NasaLocalDataSource
import com.minaev.apod.data.utils.CalendarUtil.cleanTime
import com.minaev.apod.domain.entity.ApodEntity
import com.minaev.apod.domain.entity.DomainResource
import com.minaev.apod.domain.entity.Success
import com.minaev.apod.domain.entity.errors.ErrorEntity
import com.minaev.apod.domain.repository.INasaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class NasaRepository @Inject constructor(
    private val nasaRemoteDataSource: INasaRemoteDataSource,
    private val nasaLocalDataSource: NasaLocalDataSource
): BaseRepository(), INasaRepository {

    override suspend fun getApod(): DomainResource<ApodEntity, ErrorEntity> {
        val currentDay = Calendar.getInstance().cleanTime().time
        return nasaLocalDataSource
            .getApod()
            ?.takeIf { it.dateTime == currentDay }
            ?.toDomain()
            ?.let { Success(it) }
            ?:doNetworkRequest {
                nasaRemoteDataSource
                    .getApodToday()
                    .also {
                        nasaLocalDataSource.updateApod(it.toApodDto(currentDay))
                    }
                    .toDomain()
            }
    }

    override fun getApodList(count: Int): Flow<DomainResource<List<ApodEntity>, ErrorEntity>> {
        return nasaLocalDataSource.getApods().map { apodTableList ->
            if (apodTableList.isNotEmpty()){
                Success(apodTableList.map { it.toDomain() })
            }else{
                doNetworkRequest{
                    nasaRemoteDataSource
                        .getApodList(count)
                        .map { it.toApodListDto() }
                        .also {
                            nasaLocalDataSource.updateApods(it)
                        }.map {
                            it.toDomain()
                        }
                }
            }
        }
    }

    override suspend fun refreshApodList(count: Int): DomainResource<Unit, ErrorEntity> {
        return doNetworkRequest {
            nasaRemoteDataSource
                .getApodList(count)
                .map { it.toApodListDto() }
                .also {
                    nasaLocalDataSource.updateApods(it)
                }
        }
    }
}