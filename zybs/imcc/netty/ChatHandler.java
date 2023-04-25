package com.zybs.imcc.netty;

import com.zybs.imcc.domain.ChannelRepository;
import com.zybs.imcc.proto.MsgProto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ChatHandler extends ChannelHandlerAdapter {
    private final ChannelRepository channelRepository;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MsgProto.Msg temMsg
                && !temMsg.getToId().equals("0")
                && temMsg.getType()!= MsgProto.Msg.ContentType.UNRECOGNIZED
                && temMsg.getType()!= MsgProto.Msg.ContentType.IDLE){

            log.info("user [{}] -> [{}] CHAT\n{}",temMsg.getFromId(),temMsg.getToId(),temMsg);
            Channel channel = channelRepository.get(temMsg.getToId());
            if (channel != null){
                channel.writeAndFlush(temMsg);
            }
        }

        ReferenceCountUtil.release(msg);
    }
}
