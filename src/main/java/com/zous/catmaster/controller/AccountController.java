package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.Token;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.utils.DateUtils;
import com.zous.catmaster.utils.SecurityUtils;
import com.zous.catmaster.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    ApplicationContext context;
    @Value("${spring.profiles.active}")
    String env;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Frequency(name = "login", limit = 1, time = 1)
    public Result login(@RequestParam(value = "UserName") String userName, @RequestParam("Password") String password) throws JsonProcessingException, NoSuchAlgorithmException {
        Optional<Account> optionalAccount = accountService.getAccount(userName);
        Result result;
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (SecurityUtils.md5(password).equals(account.getPassword())) {
                if (account.isActive()) {
                    if (account.getExpires() >= Calendar.getInstance().getTimeInMillis()) {
                        //SUCCESS,return sessionToken
                        result = new Result(ErrorCode.SUCCESS);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, String> map = new HashMap<>();
                        TokenUtils tokenUtils = TokenUtils.defaultUtil();
                        String token = tokenUtils.create(UUID.randomUUID().toString(), "default", String.valueOf(account.getId())).getTokenStr();
                        map.put("sessionToken", token);
                        result.setData(objectMapper.writeValueAsString(map));
                    } else {
                        result = new Result(ErrorCode.FAIL_EXPIRE_INVALID);
                        result.setDescription(context.getMessage("fail_expire_invalid",
                                new String[]{DateUtils.formatDate(account.getExpires())}, LocaleContextHolder.getLocale()));
                    }
                } else {
                    result = new Result(ErrorCode.FAIL_NOT_ACTIVITY);
                    result.setDescription(context.getMessage("fail_not_active", null, LocaleContextHolder.getLocale()));
                }
            } else {
                result = new Result(ErrorCode.FAIL_PASSWORD_ERROR);
                result.setDescription(context.getMessage("fail_password_error", null, LocaleContextHolder.getLocale()));
            }
        } else {
            result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
            result.setDescription(context.getMessage("fail_account_not_exist", null, LocaleContextHolder.getLocale()));
        }
        return result;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Frequency(name = "register", limit = 1, time = 1)
    public Result register(@RequestParam(value = "UserName") String userName, @RequestParam("Password") String password) throws NoSuchAlgorithmException {
        int code = -1;
        if ("dev".equals(env)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            Account account = new Account(userName, SecurityUtils.md5(password), true,
                    calendar.getTimeInMillis());
            code = accountService.save(account);
        } else {
            code = accountService.saveAccount(userName, SecurityUtils.md5(password));
        }
        if (code >= 0) {
            return new Result(ErrorCode.SUCCESS);
        } else {
            Result result = new Result(ErrorCode.FAIL_ACCOUNT_EXIT);
            result.setDescription(context.getMessage("fail_account_exist", null, LocaleContextHolder.getLocale()));
            return result;
        }
    }

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @Frequency(name = "refreshToken", limit = 1, time = 5)
    @CheckLogin
    public Result refreshToken(@RequestHeader(value = "Token") String token) throws JsonProcessingException {
        TokenUtils tokenUtils = TokenUtils.defaultUtil();
        Token newToken = tokenUtils.parseAndRefresh(token);
        String tokenStr = newToken.getTokenStr();
        Result result = new Result(ErrorCode.SUCCESS);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        map.put("sessionToken", tokenStr);
        result.setData(objectMapper.writeValueAsString(map));
        return result;
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @Frequency(name = "changePassword",limit = 1,time = 1)
    public void changePassword(@RequestParam("UserName") String userName,@RequestParam("Captcha")
                               String captcha,@RequestParam("newPassword")String password) {

    }
}
