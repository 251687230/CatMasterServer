package com.zous.catmaster.controller;

import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/captcha")
public class CaptchaContoller {
    @Autowired
    ApplicationContext context;
    @Autowired
    AccountService mAccountService;
    @Autowired
    CaptchaService mCaptchaService;

    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    @Frequency(name = "getCaptcha", limit = 1, time = 30)
    public Result getCaptcha(@RequestParam("PhoneNum") String phoneNum) {
        if (!isAvailable(phoneNum)) {
            Result result = new Result(ErrorCode.FAIL_INVALID_PHONENUM);
            result.setDescription(context.getMessage("fail_invalid_phonenum", null, LocaleContextHolder.getLocale()));
            return result;
        } else {
            //TODO 待接入短信验证接口
            Optional<Account> account = mAccountService.getAccount(phoneNum);
            if(account.isPresent()) {
                Random random = new Random();
                int captcha = random.nextInt(8999) + 1000;
                mCaptchaService.saveCaptcha(phoneNum,String.valueOf(captcha));
                return new Result(ErrorCode.SUCCESS);
            }else {
                Result result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
                result.setDescription(context.getMessage("fail_account_not_exist",null,LocaleContextHolder.getLocale()
                ));
                return result;
            }
        }
    }


    private boolean isAvailable(String phoneNum) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if (phoneNum.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNum);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }
}
