package com.zous.catmaster.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Teacher extends User {
    @ManyToOne(targetEntity=Manager.class)
    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
