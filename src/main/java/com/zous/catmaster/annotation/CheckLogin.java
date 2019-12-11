package com.zous.catmaster.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(2)
public @interface CheckLogin {
}
