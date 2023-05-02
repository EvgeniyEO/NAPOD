package com.minaev.apod.data.utils

import java.util.Calendar
import java.util.Date

object CalendarUtil {

    fun Calendar.cleanTime(): Date {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        return this.time
    }

}