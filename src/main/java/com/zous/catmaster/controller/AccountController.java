package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.Token;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.entity.Captcha;
import com.zous.catmaster.entity.Customer;
import com.zous.catmaster.entity.Store;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.service.CaptchaService;
import com.zous.catmaster.service.CustomerService;
import com.zous.catmaster.service.StoreService;
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
    CaptchaService captchaService;
    @Autowired
    StoreService storeService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ApplicationContext context;
    @Value("${spring.profiles.active}")
    String env;
    Gson gson = new Gson();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Frequency(name = "login", limit = 1, time = 1)
    @CheckLogin(userToken = false)
    public Result login(@RequestParam(value = "UserName") String userName, @RequestParam("Password") String password, @RequestParam("Role") String role) throws JsonProcessingException, NoSuchAlgorithmException {
        Optional<Account> optionalAccount = accountService.getAccountByUserName(userName);
        Result result;
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (password.equals(account.getPassword())) {
                //SUCCESS,return sessionToken
                if (role.equals(AppConstant.ROLE_TYPE_MANAGER)) {
                    result = new Result(ErrorCode.SUCCESS);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> map = new HashMap<>();
                    TokenUtils tokenUtils = TokenUtils.defaultUtil();
                    String token = tokenUtils.create(UUID.randomUUID().toString(), "default", String.valueOf(account.getId())).getTokenStr();
                    map.put("sessionToken", token);

                    List<Store> stores = storeService.getStores(account.getId());
                    map.put("stores", stores == null ? null : gson.toJson(stores));
                    result.setData(objectMapper.writeValueAsString(map));
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> map = new HashMap<>();
                    TokenUtils tokenUtils = TokenUtils.defaultUtil();
                    String token = tokenUtils.create(UUID.randomUUID().toString(), "default", String.valueOf(account.getId())).getTokenStr();
                    map.put("sessionToken", token);
                    result = new Result(ErrorCode.SUCCESS);
                    result.setData(objectMapper.writeValueAsString(map));
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
        try {
            accountService.createManagerAccount(userName, password);
            return new Result(ErrorCode.SUCCESS);
        } catch (Exception e) {
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
        Result result = new Result(ErrorCode.SUCCESS, "", tokenStr);
        return result;
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @Frequency(name = "changePassword", limit = 1, time = 1)
    public Result changePassword(@RequestParam("UserName") String userName, @RequestParam("NewPassword") String password) throws NoSuchAlgorithmException {
        Optional<Account> accountOptional = accountService.getAccountByUserName(userName);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setPassword(password);
            return new Result(ErrorCode.SUCCESS);
        } else {
            Result result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
            result.setDescription(context.getMessage("fail_account_not_exist", null, LocaleContextHolder.getLocale()));
            return result;
        }
    }
}
