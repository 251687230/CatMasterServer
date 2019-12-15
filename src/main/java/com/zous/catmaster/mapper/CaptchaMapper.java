package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Captcha;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaMapper extends CrudRepository<Captcha,String> {
}
