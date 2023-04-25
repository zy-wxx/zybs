package com.zybs.imcc.domain;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelRepository {
    private ConcurrentMap<String, Channel> map = new ConcurrentHashMap<>();
    public void put(String uid,Channel channel){
        map.put(uid, channel);
    }
    public Channel get(String uid){
        return map.get(uid);
    }
    public void remove(String uid){
        map.remove(uid);
    }
    public int size(){
        return map.size();
    }
}
