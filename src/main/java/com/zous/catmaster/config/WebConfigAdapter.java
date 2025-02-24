package com.zous.catmaster.config;

import com.zous.catmaster.interceptor.CheckLoginInterceptor;
import com.zous.catmaster.interceptor.FrequencyInterceptor;
import com.zous.catmaster.interceptor.RequestValidationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfigAdapter extends WebMvcConfigurationSupport {

    @Bean
    RequestValidationInterceptor getRequestValidationInterceptor(){
        return new RequestValidationInterceptor();
    }

    @Bean
    CheckLoginInterceptor getCheckLoginInterceptor(){
        return new CheckLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRequestValidationInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new FrequencyInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getCheckLoginInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
