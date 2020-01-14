package com.zous.catmaster.annotation;

import com.zous.catmaster.bean.AppConstant;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(2)
public @interface CheckLogin {
    boolean userToken() default true;
    String[] requestRoles() default {AppConstant.ROLE_TYPE_CUSTOMER,AppConstant.ROLE_TYPE_TEACHER,AppConstant.ROLE_TYPE_MANAGER};
}
