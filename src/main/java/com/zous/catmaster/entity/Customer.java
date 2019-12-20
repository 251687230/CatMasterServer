package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class Customer extends User{
    private int cardUsedTime;
    private String remarks;

    public int getCardUsedTime() {
        return cardUsedTime;
    }

    public void setCardUsedTime(int cardUsedTime) {
        this.cardUsedTime = cardUsedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
