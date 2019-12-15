package com.zous.catmaster.service;

import com.zous.catmaster.entity.Captcha;
import com.zous.catmaster.mapper.CaptchaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class CaptchaService {
    @Autowired
    CaptchaMapper captchaMapper;

    public void saveCaptcha(String phoneNum,String code){
        captchaMapper.save(new Captcha(phoneNum,code, Calendar.getInstance().getTimeInMillis()));
    }
}
