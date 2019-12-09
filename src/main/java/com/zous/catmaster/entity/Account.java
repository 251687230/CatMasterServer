package com.zous.catmaster.entity;


import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "userName")})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    private String password;
    private boolean isActive;
    private long expires;

    public Account(){}

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

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

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
