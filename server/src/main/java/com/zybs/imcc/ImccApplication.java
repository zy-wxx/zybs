package com.zybs.imcc;

import com.zybs.imcc.netty.NettyTcpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class ImccApplication {

    private final NettyTcpServer nettyTcpServer;
    public static void main(String[] args) {
        SpringApplication.run(ImccApplication.class, args);
    }
    @Bean
    public ApplicationListener<ApplicationReadyEvent>readyEventApplicationListener(){
        return new ApplicationListener<ApplicationReadyEvent>() {
            @Override
            public void onApplicationEvent(ApplicationReadyEvent event) {
                nettyTcpServer.start();
            }
        };
    }
}
