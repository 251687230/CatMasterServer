package com.zous.catmaster.entity;

import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(appliesTo = "user")
public class User {
    @Id
    private int id;
    private String userName;
    private String password;
    private String phoneNum;
    @ManyToMany(targetEntity=Role.class)
    @JoinTable(name = "role_for_account",joinColumns = {
            @JoinColumn(name="account_id",referencedColumnName = "id")
    },inverseJoinColumns = {@JoinColumn(
        name="role_type",referencedColumnName = "type"
    )})
    private Set<Role> roles = new HashSet<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
