package com.zybs.imcc.netty;

import com.zybs.imcc.domain.ChannelRepository;
import com.zybs.imcc.domain.User;
import com.zybs.imcc.domain.UserRepository;
import com.zybs.imcc.proto.MsgProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class LoginHandler extends ChannelHandlerAdapter {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final AttributeKey<User> key = AttributeKey.newInstance("USER");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("client {} connect",ctx.channel().remoteAddress().toString());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MsgProto.Msg temMsg){
            if(temMsg.getType() == MsgProto.Msg.ContentType.REQ
                    &&temMsg.hasReq()
                    &&temMsg.getReq().getCode()== MsgProto.ReqMsg.Code.LOGIN) {
                log.info("user [{}] > LOGIN\n{}", temMsg.getFromId(),temMsg);

                String uid = temMsg.getFromId();
                //binding channel with user
                User user =new User(uid,temMsg.getName());
                ctx.channel().attr(key).set(user);

                channelRepository.put(uid, ctx.channel());
                userRepository.put(uid,user);

                sendLoginRSP(ctx);
                ReferenceCountUtil.release(msg);

            }else if(temMsg.getType() == MsgProto.Msg.ContentType.REQ
                    &&temMsg.hasReq()
                    &&temMsg.getReq().getCode()== MsgProto.ReqMsg.Code.UPDATE){

                log.info("user [{}] > UPDATE\n{}",ctx.attr(key).get().getUid(),temMsg);
                sendUpdateRSP(ctx);
                ReferenceCountUtil.release(msg);
            }
            else {
                ctx.fireChannelRead(msg);
            }
        }else {
            log.info("release");
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        User user = ctx.attr(key).get();
        if (user != null){
            channelRepository.remove(user.getUid());
            userRepository.remove(user.getUid());
            log.info("user [{}] LOGOUT",user.getUid());
        }

        ctx.attr(key).set(null);
    }

    public void sendLoginRSP(ChannelHandlerContext ctx){
        MsgProto.Msg msg = MsgProto.Msg.newBuilder()
                .setFromId("0")
                .setToId(ctx.attr(key).get().getUid())
                .setType(MsgProto.Msg.ContentType.RSP)
                .setRsp(MsgProto.RspMsg.newBuilder()
                        .setCode(MsgProto.RspMsg.Code.LOGIN)
                        .setStatus(MsgProto.RspMsg.Status.SUCCESS)
                )
                .build();
        ctx.writeAndFlush(msg);
    }
    public void sendUpdateRSP(ChannelHandlerContext ctx){
        Map<String,String> tmpMap = userRepository.getUsersMap();
        MsgProto.Msg msg = MsgProto.Msg.newBuilder()
                .setFromId("0")
                .setToId(ctx.attr(key).get().getUid())
                .setType(MsgProto.Msg.ContentType.RSP)
                .setRsp(MsgProto.RspMsg.newBuilder()
                        .setStatus(MsgProto.RspMsg.Status.SUCCESS)
                        .putAllKv(tmpMap)
                        .setCode(MsgProto.RspMsg.Code.UPDATE)
                        .build()
                ).build();
        ctx.writeAndFlush(msg);
    }
}
