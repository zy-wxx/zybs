package com.example.aaaaaac.ui.chat

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.aaaaaac.database.ChatDao
import com.example.aaaaaac.database.DataBase
import com.example.aaaaaac.model.ChatInfo
import java.util.concurrent.Executors

private const val DATABASE_NAME = "chat-database"

class ChatRepository private constructor(context: Context){

    private val dataBase: DataBase = Room.databaseBuilder(
        context.applicationContext, DataBase::class.java, DATABASE_NAME)
        .build()
    private val chatDao: ChatDao = dataBase.chatDao()
    private val executor = Executors.newSingleThreadExecutor()


    /**
     * 数据库操作
     */
    //query 这里使用查询全部的查询语句
    fun getMsgList(session_string:String): LiveData<List<ChatInfo>> = chatDao.getMsg(session_string)
    //add
    fun addMsg(chatInfo: ChatInfo){
        executor.execute{
            chatDao.addMsg(chatInfo)
        }
    }


    companion object {
        private var INSTANCE: ChatRepository? = null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = ChatRepository(context)
            }
        }
        fun getInstance(): ChatRepository {
            return INSTANCE?:
            throw IllegalStateException("ChatRepository must be initialized")
        }
    }
}