package com.example.aaaaaac.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aaaaaac.net.NettyClient
import com.example.aaaaaac.proto.Chat
import com.example.aaaaaac.proto.Chat.Msg
import com.example.aaaaaac.proto.Chat.ReqMsg
import java.util.concurrent.Executors

class LoginViewModel:ViewModel(), NettyClient.OnMessageRSPListener {

    private var nettyClient:NettyClient = NettyClient.getInstance()
    var status: MutableLiveData<String?> = MutableLiveData()
    private val executor  = Executors.newSingleThreadExecutor()
    private val netError = MutableLiveData<Boolean>()

    override fun onMessage(rspMsg: Chat.RspMsg) {
        if (rspMsg.code == Chat.RspMsg.Code.LOGIN && rspMsg.status == Chat.RspMsg.Status.SUCCESS){
            status.postValue("success")
        }
        else{
            status.postValue("failed")
        }
    }
    init {
        nettyClient.setOnMessageRSPListener(this)
        executor.execute{
            try {
                nettyClient.connect()
            }catch (e: Exception) {
                netError.postValue(true)
            }
            if(nettyClient.isValid()){
                Thread.sleep(1000)
                netError.postValue(false)
            }

        }
    }
    fun getError():LiveData<Boolean> = netError

    fun login(uid:String,name: String) {
        nettyClient.setUser(uid,name)
        nettyClient.sendLogin()
    }
}