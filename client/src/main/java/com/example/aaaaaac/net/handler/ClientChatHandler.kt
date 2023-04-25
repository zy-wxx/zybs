package com.example.aaaaaac.net.handler

import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.util.ReferenceCountUtil

class ClientChatHandler(private var callback: Callback? = null) : ChannelHandlerAdapter() {


    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        super.channelRead(ctx, msg)
        callback?.onMsgReceive(msg)
        ReferenceCountUtil.release(msg)
    }

}