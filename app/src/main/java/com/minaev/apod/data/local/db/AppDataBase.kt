package com.minaev.apod.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minaev.apod.data.local.db.daos.NasaDao
import com.minaev.apod.data.local.db.entities.ApodListTableEntity
import com.minaev.apod.data.local.db.entities.ApodTableEntity

@Database(
    entities = [
        ApodListTableEntity::class,
        ApodTableEntity::class
    ],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun nasaDao(): NasaDao

    companion object {
        private const val dataBaseName = "AppDataBase"

        fun provideAppDatabase(context: Context) = Room
            .databaseBuilder(context, AppDataBase::class.java, dataBaseName)
            .fallbackToDestructiveMigration()
            .build()
    }
}