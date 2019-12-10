package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.service.UserService;
import com.zous.catmaster.utils.DateUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    UserService userService;
    @Autowired
    ApplicationContext context;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestParam(value = "UserName") String userName, @RequestParam("Password") String password) throws JsonProcessingException {
        Optional<Account> optionalAccount = userService.getAccount(userName);
        Result result;
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (MD5Encoder.encode(password.getBytes()).equals(account.getPassword())) {
                if (account.isActive()) {
                    if (account.getExpires() >= Calendar.getInstance().getTimeInMillis()) {
                        //SUCCESS,return sessionToken
                        result = new Result(ErrorCode.SUCCESS);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String,String> map = new HashMap<>();
                        map.put("sessionToken","123456");
                        result.setData(objectMapper.writeValueAsString(map));
                    } else {
                        result = new Result(ErrorCode.FAIL_EXPIRE_INVALID);
                        result.setDescription(context.getMessage("fail_expire_invalid",
                                new String[]{DateUtils.formatDate(account.getExpires())}, LocaleContextHolder.getLocale()));
                    }
                }else {
                    result = new Result(ErrorCode.FAIL_NOT_ACTIVITE);
                    result.setDescription(context.getMessage("fail_not_active",null, LocaleContextHolder.getLocale()));
                }
            } else {
                result = new Result(ErrorCode.FAIL_PASSWORD_ERROR);
                result.setDescription(context.getMessage("fail_password_error",null, LocaleContextHolder.getLocale()));
            }
        }else {
            result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
            result.setDescription(context.getMessage("fail_account_not_exist",null, LocaleContextHolder.getLocale()));
        }
        return result;
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam(value = "UserName") String userName, @RequestParam("Password") String password){
        int code = userService.saveAccount(userName,password);
        if(code >= 0){
            return "SUCCESS";
        }else {
            return "FAIL";
        }
    }
}
