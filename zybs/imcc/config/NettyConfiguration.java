package com.zybs.imcc.config;

import com.zybs.imcc.domain.ChannelRepository;
import com.zybs.imcc.domain.UserRepository;
import com.zybs.imcc.netty.MySimpleChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
public class NettyConfiguration {
    private final NettyProperties nettyProperties;

    @Bean(name = "Severbootstrap")
    public ServerBootstrap serverBootstrap(MySimpleChannelInitializer channelInitializer){
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(),workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG,nettyProperties.getBacklog());
        return b;

    }
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup(nettyProperties.getBossCount());
    }
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup(nettyProperties.getWorkerCount());
    }
    @Bean
    public InetSocketAddress tcpAddress(){
        return new InetSocketAddress(nettyProperties.getTcpPort());
    }
    @Bean
    public ChannelRepository channelRepository(){
        return new ChannelRepository();
    }
    @Bean
    public UserRepository userRepository(){return new UserRepository();}
}
