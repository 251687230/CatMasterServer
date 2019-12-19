package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Course {
    private long id;
    private String name;
    private int type;
    private int duration;
    private int maxPeople;
    private int level;
}
