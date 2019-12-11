package com.zous.catmaster.interceptor;

import com.zous.catmaster.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

public class RequestValidationInterceptor implements HandlerInterceptor {
    @Autowired
    ApplicationContext context;

    private static String VERIFE_KEY = "zous.catmaster@2019";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encoderStr = request.getHeader("EncoderStr");
        String timeStamp = request.getHeader("TimeStamp");
        String random = request.getHeader("Random");
        try {
            if(encoderStr != null && encoderStr.equalsIgnoreCase(SecurityUtils.md5(timeStamp + random + VERIFE_KEY))){
                return true;
            }else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,context.getMessage("fail_bad_requeset",null, LocaleContextHolder.getLocale()));
            }
        } catch (NoSuchAlgorithmException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,context.getMessage("fail_server_error",null, LocaleContextHolder.getLocale()));
        }
        return false;
    }
}
