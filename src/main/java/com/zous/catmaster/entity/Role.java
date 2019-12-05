package com.zous.catmaster.entity;

import org.hibernate.annotations.Table;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Table(appliesTo="role")
public class Role {
    public static int ROLE_MASTER = 1;

    @Id
    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role(){}

    public Role(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
