package com.zous.catmaster.controller;

import com.zous.catmaster.entity.User;
import com.zous.catmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    UserService userService;

    @RequestMapping("login")
    public String login(String userName,String password){
        User user = userService.getUser(userName,password);
        if(user != null){
            return "SUCCESS";
        }else {
            return "FAIL";
        }
    }
}
