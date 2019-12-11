package com.zous.catmaster.config;

import com.zous.catmaster.interceptor.FrequencyInterceptor;
import com.zous.catmaster.interceptor.RequestValidationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfigAdapter extends WebMvcConfigurerAdapter {

    @Bean
    RequestValidationInterceptor getRequestValidationInterceptor(){
        return new RequestValidationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRequestValidationInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
