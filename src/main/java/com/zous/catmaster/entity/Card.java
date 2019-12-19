package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Card {
    private long id;
    private int type;
    private String name;
    private String imageUrl;
    private float price;
    private int totalTimes;
    private long expireTime;
}
