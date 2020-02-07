package com.zous.catmaster.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Customer  {
    @Id
    private AccountStoreMapper accountStoreMapper;

    private String name;

    private String headIcon;

    private long birthday;

    private long joinTime;

    private boolean sex;

    private String remarks;
    @ManyToMany(targetEntity = Card.class)
    private Set<Card> ownCard = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
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

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public AccountStoreMapper getAccountStoreMapper() {
        return accountStoreMapper;
    }

    public void setAccountStoreMapper(AccountStoreMapper accountStoreMapper) {
        this.accountStoreMapper = accountStoreMapper;
    }
}
