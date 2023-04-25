package com.example.aaaaaac.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aaaaaac.model.Session
import java.util.UUID

@Dao
interface SessionDao {
    @Query("SELECT * FROM session_info WHERE from_id = :uid")
    fun getSessions(uid:String):LiveData<List<Session>>
    @Query("SELECT * FROM session_info WHERE session_string = :string")
    fun getSession(string:String):Session?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSession(session: Session)
    @Update
    fun updateSession(session: Session)
    @Delete
    fun deleteSession(session: Session)
}