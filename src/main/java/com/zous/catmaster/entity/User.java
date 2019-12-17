package com.zous.catmaster.entity;

import javax.persistence.Entity;

@Entity
public class User {
    private String nickName;
    private String headPhoto;

    private Role role;
}
