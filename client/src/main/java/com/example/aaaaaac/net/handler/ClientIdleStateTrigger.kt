package com.example.aaaaaac.net.handler

import com.example.aaaaaac.proto.Chat.Msg
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext

class ClientIdleStateTrigger : ChannelHandlerAdapter() {
    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        super.userEventTriggered(ctx, evt)
        val msg = Msg.newBuilder()
            .setType(Msg.ContentType.IDLE)
            .build()
        ctx.writeAndFlush(msg)
    }
}