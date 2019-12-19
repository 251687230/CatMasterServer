package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Manager {
    private long id;
    private String nickName;
    private String headPhoto;
    private long birthday;
    private int sex;
    private String phoneNum;
    private long expireTime = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
