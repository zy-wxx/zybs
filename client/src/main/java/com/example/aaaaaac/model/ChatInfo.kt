package com.example.aaaaaac.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "chat_info")
data class ChatInfo(
    var session_string: String,
    var text: String,
    var viewType: Int = 1,
    var date: Date = Date(),
    @PrimaryKey(autoGenerate = true)
    val id :Long = 0
)