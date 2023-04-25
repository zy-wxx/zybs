package com.zybs.imcc.netty;

import com.zybs.imcc.proto.MsgProto;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MySimpleChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final LoginHandler loginHandler;
    private final ChatHandler chatHandler;
    private final UserEventHandler userEventHandler;
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //入站handler ：变长帧头解码器
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //入站handler：帧解码器
        pipeline.addLast(new ProtobufDecoder(MsgProto.Msg.getDefaultInstance()));
        //出站handler：变长帧头编码器
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        //出站handler：帧编码器
        pipeline.addLast(new ProtobufEncoder());
        //触发心跳事件
        pipeline.addLast(new IdleStateHandler(5*60,0,0));
        //处理心跳事件
        pipeline.addLast(userEventHandler);
        //处理登录消息和更新消息
        pipeline.addLast(loginHandler);
        //处理聊天消息、请求消息、响应消息
        pipeline.addLast(chatHandler);

    }
}
