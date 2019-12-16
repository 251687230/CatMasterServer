package com.zous.catmaster.service;

import com.zous.catmaster.entity.Captcha;
import com.zous.catmaster.mapper.CaptchaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class CaptchaService {
    @Autowired
    CaptchaMapper captchaMapper;

    public void saveCaptcha(String phoneNum,String code){
        captchaMapper.save(new Captcha(phoneNum,code, Calendar.getInstance().getTimeInMillis()));
    }

    public Optional<Captcha> validate(String phoneNum,String captcha){
        return Optional.ofNullable(captchaMapper.findByUserNameAndCaptcha(phoneNum,captcha));
    }

    public void delete(Captcha captcha){
        captchaMapper.delete(captcha);
    }
}
