package com.example.aaaaaac.ui.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aaaaaac.model.UserInfo
import com.example.aaaaaac.net.NettyClient
import com.example.aaaaaac.proto.Chat
import com.example.aaaaaac.proto.Chat.RspMsg
private const val TAG="UserListViewModel"
class UserListViewModel:ViewModel(), NettyClient.OnMessageRSPListener {
    private val nettyClient = NettyClient.getInstance()
    private val userOnlineList:MutableList<UserInfo> = mutableListOf()
    private val userListLiveData:MutableLiveData<List<UserInfo>> = MutableLiveData()

    init {
        nettyClient.setOnMessageRSPListener(this)
    }
    fun getUserListLiveData():LiveData<List<UserInfo>> = userListLiveData

    override fun onMessage(rspMsg: RspMsg) {
        if (rspMsg.code == RspMsg.Code.UPDATE
            &&rspMsg.status == RspMsg.Status.SUCCESS){
            updateUserList(rspMsg)
            Log.e("UserListViewModel",rspMsg.toString())
        }
    }
    private fun updateUserList(rspMsg: RspMsg){
        userOnlineList.clear()
        rspMsg.kvMap.forEach{
            userOnlineList.add(UserInfo(it.key,it.value))
        }
        Log.e(TAG,"online = ${userOnlineList.size}")
        userListLiveData.postValue(userOnlineList)
    }




    fun sendREQ(){
        if (!nettyClient.isValid()) {
            Thread {
                nettyClient.connect()
                nettyClient.sendLogin()
                nettyClient.sendREQ()
            }.start()
            return
        }
        nettyClient.sendREQ()

    }

}