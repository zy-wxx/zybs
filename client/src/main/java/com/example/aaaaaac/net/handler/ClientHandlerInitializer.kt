package com.example.aaaaaac.net.handler

import com.example.aaaaaac.proto.Chat
import io.netty.channel.*
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import io.netty.handler.timeout.IdleStateHandler

class ClientHandlerInitializer(private var callback: Callback) :
    ChannelInitializer<Channel>() {

    @Throws(Exception::class)
    override fun initChannel(ch: Channel?) {
        val pipeline = ch!!.pipeline()
        pipeline.addLast(ProtobufVarint32FrameDecoder())
        pipeline.addLast(ProtobufDecoder(Chat.Msg.getDefaultInstance()))
        pipeline.addLast(ProtobufVarint32LengthFieldPrepender())
        pipeline.addLast(ProtobufEncoder())

        // pipeline.addLast(new ClientLoginHandler());
        pipeline.addLast(ClientChatHandler(callback))
        pipeline.addLast(IdleStateHandler(0, 0, 30))
        pipeline.addLast(ClientIdleStateTrigger())
    }
}