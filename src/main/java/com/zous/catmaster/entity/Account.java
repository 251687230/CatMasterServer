package com.zous.catmaster.entity;


import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ListIndexBase;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userName")})
public class Account {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    private String userName;
    private String password;
    private long createTime;
    @ElementCollection(targetClass=String.class,fetch = FetchType.EAGER)
    private Set<String> roleTypes = new HashSet<>();

    public Account(){}

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Set<String> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(Set<String> roleTypes) {
        this.roleTypes = roleTypes;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
