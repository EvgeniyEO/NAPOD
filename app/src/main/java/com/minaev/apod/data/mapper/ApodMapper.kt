package com.minaev.apod.data.mapper

import com.minaev.apod.data.local.db.entities.ApodDto
import com.minaev.apod.data.local.db.entities.ApodListTableEntity
import com.minaev.apod.data.local.db.entities.ApodTableEntity
import com.minaev.apod.data.remote.model.ApodResponse
import com.minaev.apod.domain.entity.ApodEntity
import java.util.Calendar

fun ApodDto.toDomain(): ApodEntity {
    return ApodEntity(
        this.imgUrl,
        this.title,
        this.date,
        this.explanation
    )
}

fun ApodResponse.toDomain(): ApodEntity {
    return ApodEntity(
        imgUrl = this.hdurl?:this.url?:"",
        title = this.title?:"",
        date = this.date?:"",
        explanation = this.explanation?:""
    )
}

fun ApodResponse.toApodListDto(): ApodListTableEntity {
    return ApodListTableEntity(
        imgUrl = this.hdurl?:this.url?:"",
        title = this.title?:"",
        date = this.date?:"",
        explanation = this.explanation?:""
    )
}

fun ApodResponse.toApodDto(currentDate: Long): ApodTableEntity {
    return ApodTableEntity(
        dateTime = currentDate,
        imgUrl = this.hdurl?:this.url?:"",
        title = this.title?:"",
        date = this.date?:"",
        explanation = this.explanation?:""
    )
}

