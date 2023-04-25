package com.example.aaaaaac.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.aaaaaac.model.ChatInfo
import java.util.*

@Dao
interface ChatDao {
    @Query("SELECT *  FROM chat_info WHERE session_string = (:string) ")
    fun getMsg(string: String): LiveData<List<ChatInfo>>
    @Insert
    fun addMsg(msg: ChatInfo)
    @Update
    fun update(msg: ChatInfo)
}