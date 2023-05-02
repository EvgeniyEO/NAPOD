package com.minaev.apod.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ApodListTableEntity.tableName, primaryKeys = ["date"])
data class ApodListTableEntity(
    override val imgUrl : String,
    override val title: String,
    override val date: String,
    override val explanation: String
): ApodDto(){
    companion object{
        const val tableName = "apods"
    }
}

@Entity(tableName = ApodTableEntity.tableName)
data class ApodTableEntity(
    @PrimaryKey val id: Int = apodId,
    val dateTime: Long,
    override val imgUrl : String,
    override val title: String,
    override val date: String,
    override val explanation: String
): ApodDto(){
    companion object{
        const val tableName = "apod"
        const val apodId = 1
    }
}

open class ApodDto{
    open val imgUrl : String = ""
    open val title: String = ""
    open val date: String = ""
    open val explanation: String = ""
}

