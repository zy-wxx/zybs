package com.example.aaaaaac.ui.session

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.aaaaaac.database.DataBase
import com.example.aaaaaac.model.Session
import com.example.aaaaaac.model.UserInfo
import java.util.UUID
import java.util.concurrent.Executors
private const val DATABASE_NAME = "session-database"
class SessionRepository private constructor(context: Context){

    private var dataBase: DataBase = Room.databaseBuilder(
        context.applicationContext,
        DataBase::class.java,
        DATABASE_NAME
    ).build()
    private var sessionDao = dataBase.sessionDao()
    private val executor = Executors.newSingleThreadExecutor()
    fun getSessions(userInfo: UserInfo): LiveData<List<Session>> = sessionDao.getSessions(userInfo.uid)
    private fun getSession(string: String):Session? = sessionDao.getSession(string)
    fun addSession(session: Session){
        executor.execute{
            val dbSession = getSession(session.session_string)
            if (dbSession == null){
                sessionDao.addSession(session)
            }else{
                sessionDao.updateSession(session)
            }
        }
    }

    fun deleteSession(session: Session){
        executor.execute{
            sessionDao.deleteSession(session)
        }
    }



    companion object {
        private var INSTANCE: SessionRepository? = null
        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = SessionRepository(context)
            }
        }
        fun get(): SessionRepository {
            return INSTANCE?:
            throw IllegalStateException("SessionRepository must be initialized")
        }
    }
}