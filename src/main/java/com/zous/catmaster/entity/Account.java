package com.zous.catmaster.entity;


import org.hibernate.annotations.ListIndexBase;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userName")})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;
    private String userId;
    @ElementCollection(targetClass=String.class)
    private Set<String> roleTypes = new HashSet<>();

    public Account(){}

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(Set<String> roleTypes) {
        this.roleTypes = roleTypes;
    }
}
