package com.example.aaaaaac.net.handler

import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext

class ClientLoginHandler : ChannelHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        super.channelRead(ctx, msg)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
    }
}