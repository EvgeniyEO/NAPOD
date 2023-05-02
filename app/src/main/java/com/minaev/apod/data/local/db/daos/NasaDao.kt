package com.minaev.apod.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.minaev.apod.data.local.db.entities.ApodListTableEntity
import com.minaev.apod.data.local.db.entities.ApodTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NasaDao {

    @Query("SELECT * FROM ${ApodListTableEntity.tableName}")
    fun getApods(): Flow<List<ApodListTableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApods(apods: List<ApodListTableEntity>)

    @Query("DELETE FROM ${ApodListTableEntity.tableName}")
    suspend fun deleteAllApods()

    @Transaction
    suspend fun updateAllApods(apods: List<ApodListTableEntity>){
        deleteAllApods()
        insertApods(apods)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateApod(apod: ApodTableEntity)

    @Query("SELECT * FROM ${ApodTableEntity.tableName} where id = ${ApodTableEntity.apodId}")
    suspend fun getApod(): ApodTableEntity?

}