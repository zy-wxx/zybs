package com.zybs.imcc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
@Slf4j
@Component
@RequiredArgsConstructor
public class NettyTcpServer {
    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress tcpAddress;
    private Channel channel;
    public void start(){
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(tcpAddress).sync();
            log.info("[tcp server] run on port [{}]", tcpAddress.getPort());


            channel = channelFuture.channel().closeFuture().sync().channel();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @PreDestroy
    public void stop(){
        if (channel != null){
            channel.close();
            channel.parent().close();
        }
    }


}
