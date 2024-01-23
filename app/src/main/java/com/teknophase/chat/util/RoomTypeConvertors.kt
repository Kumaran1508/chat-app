package com.teknophase.chat.util

import androidx.room.TypeConverter
import java.util.Date

class RoomTypeConvertors {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}