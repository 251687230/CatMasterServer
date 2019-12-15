package com.zous.catmaster.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Captcha {
    @Id
    private String userName;
    private String captcha;
    private long createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Captcha(){

    }

    public Captcha(String userName, String captcha, long createTime) {
        this.userName = userName;
        this.captcha = captcha;
        this.createTime = createTime;
    }
}
