package com.example.aaaaaac.net

import com.example.aaaaaac.model.UserInfo
import com.example.aaaaaac.net.handler.Callback
import com.example.aaaaaac.proto.Chat
import com.example.aaaaaac.proto.Chat.Msg
import com.example.aaaaaac.proto.Chat.ReqMsg
import com.example.aaaaaac.proto.Chat.RspMsg
import com.example.aaaaaac.proto.Chat.TextMsg
import io.netty.channel.Channel
private const val TAG = "NettyClient"
class NettyClient private constructor() : Callback{

    private var bootStrap: ClientBootStrap = ClientBootStrap(this)
    private var channel: Channel? = null
    private var userinfo:UserInfo? = null

    fun isValid():Boolean{
        if (channel != null && channel!!.isActive){
            return true
        }
        return false
    }
    /**
     * onReq
     */
    private var onMessageREQListener:OnMessageREQListener? = null;
    fun setOnMessageREQListener(onMessageREQListener: OnMessageREQListener){
        this.onMessageREQListener = onMessageREQListener
    }
    interface OnMessageREQListener{
        fun onMessage(reqMsg: ReqMsg)
    }
    /**
     * onRSP
     */
    private var onMessageRSPListener:OnMessageRSPListener? = null;
    fun setOnMessageRSPListener(onMessageRSPListener: OnMessageRSPListener){
        this.onMessageRSPListener = onMessageRSPListener
    }
    interface OnMessageRSPListener{
        fun onMessage(rspMsg: RspMsg)
    }
    /**
     * onCHAT
     */
    private var onMessageTEXTListener:OnMessageTEXTListener? = null;
    fun setOnMessageTEXTListener(onMessageTEXTListener: OnMessageTEXTListener){
        this.onMessageTEXTListener = onMessageTEXTListener
    }
    interface OnMessageTEXTListener{
        fun onMessage(textMsg: TextMsg)
    }




    fun setUser(uid:String,name:String){
        userinfo = UserInfo(uid,name)
    }

    fun sendLogin(){
        val reqMsg:ReqMsg = ReqMsg.newBuilder().apply {
            code = ReqMsg.Code.LOGIN
            extra = userinfo?.name
        }.build()
        val msg:Msg = Msg.newBuilder().apply {
            fromId = userinfo?.uid
            toId = "0"
            name = userinfo?.name
            type = Msg.ContentType.REQ
            req = reqMsg
        }.build()
        writeMsg(msg)
    }
    fun sendREQ(){
        val reqMsg = ReqMsg.newBuilder().apply {
            code = ReqMsg.Code.UPDATE
            extra = userinfo?.name
        }.build()
        val msg = Msg.newBuilder().apply {
            fromId = userinfo?.uid
            toId = "0"
            type = Msg.ContentType.REQ
            req = reqMsg
        }.build()
        writeMsg(msg)
    }
    fun writeMsg(msg: Msg) {
        channel?.writeAndFlush(msg)

    }
    override fun onMsgReceive(msg: Any) {
        if (msg is Msg){
            when(msg.type){
                Msg.ContentType.TEXT-> onMessageTEXTListener?.onMessage(msg.textMsg)
                Msg.ContentType.REQ-> onMessageREQListener?.onMessage(msg.req)
                Msg.ContentType.RSP-> onMessageRSPListener?.onMessage(msg.rsp)
                else->{}
            }
        }
    }


    companion object {
        private var INSTANCE: NettyClient? = null
        fun getInstance(): NettyClient{
             if (INSTANCE == null){
                INSTANCE = NettyClient()
            }
            return INSTANCE?:
            throw IllegalStateException("NettyClient init failed")
        }
    }

    fun close(){
        bootStrap.close()
    }
    fun connect() {
        try {
            channel = bootStrap.connect()
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}