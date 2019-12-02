package com.zous.catmaster.entity;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(appliesTo="role")
public class Role {
    private int type;
    private String name;
    @ManyToMany(targetEntity=User.class,mappedBy="roles")
    private Set<User> users = new HashSet<>();

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
