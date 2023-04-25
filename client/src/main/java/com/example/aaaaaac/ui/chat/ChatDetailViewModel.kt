package com.example.aaaaaac.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aaaaaac.model.ChatInfo
import com.example.aaaaaac.model.Session
import com.example.aaaaaac.net.NettyClient
import com.example.aaaaaac.proto.Chat
import com.example.aaaaaac.proto.Chat.TextMsg


private const val TAG = "ChatViewModel"
class ChatDetailViewModel(): ViewModel(),NettyClient.OnMessageTEXTListener {
    private val chatRepository : ChatRepository = ChatRepository.getInstance()
    private var nettyClient: NettyClient = NettyClient.getInstance()
    lateinit var session:Session

    var chatMsgList :LiveData<List<ChatInfo>> = MutableLiveData()



    init {
        nettyClient.setOnMessageTEXTListener(this)
    }


    fun initSession(session: Session){
        this.session = session
        updateMsgList()
    }

    private fun updateMsgList() {
        Log.e(TAG,session.toString())
        chatMsgList = chatRepository.getMsgList(session.session_string)
    }

    override fun onMessage(textMsg: TextMsg) {

        val chatInfo = ChatInfo(session.session_string,textMsg.text, 2 )
        writeToRepository(chatInfo)
    }

    private fun writeToRepository(text:String){
        val chatInfo = ChatInfo(session.session_string,text,1 )
        writeToRepository(chatInfo)
    }
    private fun writeToRepository(chatInfo: ChatInfo){
        chatRepository.addMsg(chatInfo)
        updateMsgList()
    }

    fun writeToRemote(text: String){

        val tempMsg = Chat.Msg.newBuilder().apply {
            fromId = session.from_id
            toId = session.to_id
            name = session.from_name
            type = Chat.Msg.ContentType.TEXT
            setTextMsg(TextMsg.newBuilder().apply {
                this.text = text
            })
            build()
        }.build()
        if (nettyClient.isValid()) {
            nettyClient.writeMsg(tempMsg)
        }
        else{
            Thread{
                nettyClient.connect()
                nettyClient.sendLogin()
                nettyClient.writeMsg(tempMsg)
            }.start()
        }
        writeToRepository(text)
    }





}