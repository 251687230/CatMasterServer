package com.zous.catmaster.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer  {
    @Id
    private AccountStoreKey accountStoreKey;
    @NotBlank
    private String name;

    private String headIcon;
    @NotBlank
    private String phoneNum;

    private long birthday;

    private long joinTime;

    private int sex;

    private String remarks;
    @ManyToMany(targetEntity = Card.class,fetch = FetchType.EAGER )
    private Set<Card> ownCard = new HashSet<>();

    private int credit;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
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

    public AccountStoreKey getAccountStoreKey() {
        return accountStoreKey;
    }

    public void setAccountStoreKey(AccountStoreKey accountStoreKey) {
        this.accountStoreKey = accountStoreKey;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
