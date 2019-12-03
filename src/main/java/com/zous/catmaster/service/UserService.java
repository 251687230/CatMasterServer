package com.zous.catmaster.service;

import com.zous.catmaster.entity.User;
import com.zous.catmaster.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUser(String userName, String password){
        return userMapper.findByUserNameAndPassword(userName,password);
    }
}
