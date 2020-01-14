package com.zous.catmaster.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.SmsResult;
import com.zous.catmaster.entity.Captcha;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private Gson gson = new Gson();
    Logger logger = LoggerFactory.getLogger(CaptchaContoller.class);

    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    @Frequency(name = "getCaptcha", limit = 1, time = 30)
    public Result getCaptcha(@RequestParam("PhoneNum") String phoneNum) {
        if (!isAvailable(phoneNum)) {
            Result result = new Result(ErrorCode.FAIL_INVALID_PHONENUM);
            result.setDescription(context.getMessage("fail_invalid_phonenum", null, LocaleContextHolder.getLocale()));
            return result;
        } else {
            Random random = new Random();
            int captcha = random.nextInt(899999) + 100000;

            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                    "LTAI4FjF8qHLRhQRd5ed1sXo", "FZUWpxf52JlrrEhJxpNIOFcp8GZHca");
            IAcsClient client = new DefaultAcsClient(profile);
            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dysmsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", phoneNum);
            request.putQueryParameter("SignName", "喵管家");
            request.putQueryParameter("TemplateCode", "SMS_182542123");
            request.putQueryParameter("TemplateParam", "{\"code\":\"" + captcha + "\"}");
            try {
                CommonResponse response = client.getCommonResponse(request);
                SmsResult smsResult = gson.fromJson(response.getData(), SmsResult.class);
                if (smsResult.getCode().equals("OK")) {
                    mCaptchaService.saveCaptcha(phoneNum, String.valueOf(captcha));
                    return new Result(ErrorCode.SUCCESS);
                } else {
                    logger.error(response.getData());
                }
            } catch (ClientException e) {
               logger.error("短信请求失败",e);
            }
            return new Result(ErrorCode.FAIL_GET_CAPTCHA_EXCEPTION, context.getMessage("fail_get_captcha_exception",null,
                    LocaleContextHolder.getLocale()));
        }
    }

    @RequestMapping(value = "/verifyCaptcha",method = RequestMethod.GET)
    @Frequency(name="verifyCaptcha",limit = 1,time = 1)
    public Result verifyCaptcha(@RequestParam("PhoneNum") String phoneNum,@RequestParam("Captcha") String captcha){
        Optional<Captcha> captchaOptional = mCaptchaService.validate(phoneNum, captcha);
        if(captchaOptional.isPresent()){
            Captcha queryCaptcha = captchaOptional.get();
            if(Calendar.getInstance().getTimeInMillis() - queryCaptcha.getCreateTime() > 5 * 60 * 1000){
                return new Result(ErrorCode.FAIL_CAPTCHA_TIMEOUT,context.getMessage("fail_captcha_timeout",null,LocaleContextHolder.getLocale()));
            }else {
                return new Result(ErrorCode.SUCCESS);
            }
        }else {
            return new Result(ErrorCode.FAIL_CAPTCHA_ERROR,context.getMessage("fail_captcha_error",null,LocaleContextHolder.getLocale()));
        }
    }

    private boolean isAvailable(String phoneNum) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,1,5-9])|(19[1,9]))\\d{8}$";
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
