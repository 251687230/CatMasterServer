package com.zous.catmaster.annotation;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(1)
public @interface Frequency {
    String name() default "all";
    /**
     *
     * 允许访问的次数，默认值MAX_VALUE
     */
    int limit() default Integer.MAX_VALUE;

    /**
     *
     * 时间段，单位为秒，默认值一分钟
     */
    int time() default 60;

}
