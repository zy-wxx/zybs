package com.zybs.imcc.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserRepository {
    private ConcurrentMap<String,User> userMap = new ConcurrentHashMap<>();


    public User get(String uid){
        return userMap.get(uid);
    }
    public void put(String uid, User user){
        userMap.put(uid, user);
    }
    public int getSize(){
        return userMap.size();
    }
    public void remove(String uid){
        userMap.remove(uid);
    }
    public Map<String,String> getUsersMap(){
        Map<String,String> map = new HashMap<>();
        userMap.forEach((k,v)->{
            map.put(v.getUid(), v.getName());
        });
        return map;
    }


}
