package com.example.aaaaaac.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.aaaaaac.model.UserInfo
import java.util.Date
import java.util.UUID

@TypeConverters
class DataTypeConverters {
    @TypeConverter
    fun fromDate(date:Date?):Long?{
        return date?.time
    }
    @TypeConverter
    fun toDate(millis:Long?):Date?{
        return millis?.let { Date(it) }
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
    @TypeConverter
    fun toUUID(uuid:String?):UUID?{
        return UUID.fromString(uuid)
    }


}