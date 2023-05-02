package com.minaev.apod.presentation.feature

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.minaev.apod.R

enum class ScreenType(@DrawableRes val iconId: Int, @StringRes val titleId: Int) {
    Today(R.drawable.ic_daily, R.string.tab_item_today_title),
    List(R.drawable.ic_list, R.string.tab_item_list_title);

    companion object{
        fun getScreenList() = values().toList()

        fun getScreenTypeByIndex(index: Int): ScreenType {
            return values()[index]
        }
    }
}