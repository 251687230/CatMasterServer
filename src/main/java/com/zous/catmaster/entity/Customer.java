package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Customer extends User{
    private int cardUsedTime;

    public int getCardUsedTime() {
        return cardUsedTime;
    }

    public void setCardUsedTime(int cardUsedTime) {
        this.cardUsedTime = cardUsedTime;
    }
}
