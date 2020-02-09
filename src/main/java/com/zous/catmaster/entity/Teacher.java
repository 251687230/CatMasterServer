package com.zous.catmaster.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher {
    @Id
    private AccountStoreKey accountStoreKey;
    @NotBlank
    private String name;

    private String headIcon;
    @NotBlank
    private String phoneNum;

    private boolean sex;

    private long birthday;

    private String introduce;

    @OneToMany(targetEntity = Course.class,fetch = FetchType.EAGER)
    private Set<Course> courseSet = new HashSet<>();


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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public Set<Course> getCourseSet() {
        return courseSet;
    }

    public void setCourseSet(Set<Course> courseSet) {
        this.courseSet = courseSet;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public AccountStoreKey getAccountStoreKey() {
        return accountStoreKey;
    }

    public void setAccountStoreKey(AccountStoreKey accountStoreKey) {
        this.accountStoreKey = accountStoreKey;
    }
}
