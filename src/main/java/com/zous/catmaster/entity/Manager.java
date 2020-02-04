package com.zous.catmaster.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Manager extends User{

    private long expireTime = -1;
    @OneToMany(targetEntity=Teacher.class,fetch = FetchType.EAGER)
    private Set<Teacher> teachers = new HashSet<>();
    @OneToMany(targetEntity = Store.class,fetch = FetchType.EAGER)
    private Set<Store> stores = new HashSet<>();

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Store> getStores() {
        return stores;
    }

    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }
}
