package com.zybs.imcc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {

    //tcp
    private int tcpPort = 8899;
    private int bossCount=1;
    private int workerCount=3;
    private boolean keepalive = true;
    private int backlog = 5;


}

