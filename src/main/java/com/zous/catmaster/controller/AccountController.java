package com.zous.catmaster.controller;

import com.zous.catmaster.entity.Account;
import com.zous.catmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(value = "username") String userName, @RequestParam("password") String password){
        Account account = userService.getUser(userName,password);
        if(account != null){
            return "SUCCESS";
        }else {
            return "FAIL";
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam(value = "username") String userName, @RequestParam("password") String password){
        int code = userService.saveUser(userName,password);
        if(code >= 0){
            return "SUCCESS";
        }else {
            return "FAIL";
        }
    }
}
