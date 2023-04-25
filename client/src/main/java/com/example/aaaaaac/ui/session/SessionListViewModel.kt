package com.example.aaaaaac.ui.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aaaaaac.model.Session
import com.example.aaaaaac.model.UserInfo
import com.example.aaaaaac.net.NettyClient
import com.example.aaaaaac.proto.Chat.Msg
import com.example.aaaaaac.proto.Chat.ReqMsg
import com.example.aaaaaac.proto.Chat.RspMsg

class SessionListViewModel:ViewModel(){
    private var sessionRepository = SessionRepository.get()
    var userList:LiveData<List<Session>> = MutableLiveData()
    private lateinit var userInfo: UserInfo

    fun initUserInfo(userInfo: UserInfo){
        this.userInfo = userInfo
        updateUserList()
    }

    private fun updateUserList(){
        userList = sessionRepository.getSessions(userInfo)
    }
    fun delete(session: Session){
        sessionRepository.deleteSession(session)
    }

}