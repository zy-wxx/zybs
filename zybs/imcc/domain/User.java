package com.zybs.imcc.domain;

public class User {

    private final String uid;
    private final String name;

    public User(String uid,String name) {
        this.uid = uid;
        this.name = name;
    }
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User that){
            return uid.equals(that.uid);
        }
        return false;
    }
}
