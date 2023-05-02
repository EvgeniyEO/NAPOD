package com.minaev.apod.data.repository.datasource

import com.minaev.apod.data.local.db.daos.NasaDao
import com.minaev.apod.data.local.db.entities.ApodListTableEntity
import com.minaev.apod.data.local.db.entities.ApodTableEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NasaLocalDataSource @Inject constructor(
    private val nasaDao: NasaDao
) {
    fun getApods() = nasaDao.getApods().flowOn(Dispatchers.IO)

    suspend fun updateApods(apods: List<ApodListTableEntity>){
        withContext(Dispatchers.IO){
            nasaDao.updateAllApods(apods)
        }
    }

    suspend fun getApod() = withContext(Dispatchers.IO){
        nasaDao.getApod()
    }


    suspend fun updateApod(apod: ApodTableEntity) = withContext(Dispatchers.IO){
        nasaDao.updateApod(apod)
    }

}