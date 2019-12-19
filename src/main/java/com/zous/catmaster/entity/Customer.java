package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Customer {
    private long id;
    private String nickName;
    private String headPhoto;
    private long birthday;
    private int sex;
    private String phoneNum;
    private int cardUsedTime;
}
