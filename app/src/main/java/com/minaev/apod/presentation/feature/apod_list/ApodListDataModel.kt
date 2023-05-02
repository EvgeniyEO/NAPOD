package com.minaev.apod.presentation.feature.apod_list

import com.minaev.apod.domain.entity.ApodEntity

sealed class ApodListDataModel {
    data class ApodListData(val apodList: List<ApodEntity>): ApodListDataModel()
    object ApodListEmpty: ApodListDataModel()
    object ApodListError: ApodListDataModel()
}

fun List<ApodEntity>.toUiDataModel(): ApodListDataModel {
    return if (this.isNotEmpty())
        ApodListDataModel.ApodListData(this)
    else
        ApodListDataModel.ApodListEmpty
}