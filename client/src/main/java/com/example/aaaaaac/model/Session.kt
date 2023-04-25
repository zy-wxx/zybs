package com.example.aaaaaac.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity(tableName = "session_info")
data class Session (
    var from_id:String,
    var from_name:String,
    var to_id:String,
    var to_name:String,
    @PrimaryKey
    var session_string:String ="$from_id<->$to_id",
    var date: Date = Date()
) : Serializable
