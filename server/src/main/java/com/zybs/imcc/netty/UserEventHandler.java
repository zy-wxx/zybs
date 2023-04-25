package com.zybs.imcc.netty;

import com.zybs.imcc.domain.ChannelRepository;
import com.zybs.imcc.domain.User;
import com.zybs.imcc.domain.UserRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class UserEventHandler extends ChannelHandlerAdapter {

    private final AttributeKey<User> key = AttributeKey.valueOf("USER");
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        ctx.channel().close();
        log.info("user [{}] write timeout",ctx.attr(key).get().getName());
    }
}
