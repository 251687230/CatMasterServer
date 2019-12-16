package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Captcha;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CaptchaMapper extends CrudRepository<Captcha,String> {
    Optional<Captcha> findByUserId(long userId);
}
