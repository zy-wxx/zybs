package com.example.aaaaaac.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aaaaaac.model.ChatInfo
import com.example.aaaaaac.model.Session

@Database(entities = [Session::class,ChatInfo::class], version = 1, exportSchema = false)
@TypeConverters(DataTypeConverters::class)
abstract class DataBase: RoomDatabase() {
    abstract fun sessionDao():SessionDao
    abstract fun chatDao():ChatDao
}