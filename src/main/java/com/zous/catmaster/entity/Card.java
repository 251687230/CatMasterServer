package com.zous.catmaster.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Card {
    @Id
    private long id;
    private String name;
    private String imageUrl;
    private float price;
    private long expireTime;
    private int totalTimes;
    private boolean isExperienceCard;
    @ManyToMany(targetEntity = Course.class)
    @JoinTable(name = "card_course",joinColumns = @JoinColumn(name = "card_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "course_id",referencedColumnName = "id"))
    private Set<Course> applyCources = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }


    public boolean isExperienceCard() {
        return isExperienceCard;
    }

    public void setExperienceCard(boolean experienceCard) {
        isExperienceCard = experienceCard;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Set<Course> getApplyCources() {
        return applyCources;
    }

    public void setApplyCources(Set<Course> applyCources) {
        this.applyCources = applyCources;
    }
}
