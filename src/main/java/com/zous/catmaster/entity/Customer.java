package com.zous.catmaster.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Customer extends User{
    private int cardUsedTime;
    private String remarks;
    @ManyToMany(targetEntity = Card.class)
    @JoinTable(name = "customer_card",joinColumns=@JoinColumn(name = "card_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"))
    private Set<Card> ownCard = new HashSet<>();

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

    public Set<Card> getOwnCard() {
        return ownCard;
    }

    public void setOwnCard(Set<Card> ownCard) {
        this.ownCard = ownCard;
    }
}
