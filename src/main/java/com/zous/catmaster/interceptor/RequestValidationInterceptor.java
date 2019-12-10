package com.zous.catmaster.interceptor;

import com.zous.catmaster.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
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
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rep = (HttpServletResponse)response;
        String encoderStr = req.getHeader("EncoderStr");
        String timeStamp = req.getHeader("TimeStamp");
        String random = req.getHeader("Random");
        try {
            if(encoderStr != null && encoderStr.equalsIgnoreCase(SecurityUtils.md5(timeStamp + random + VERIFE_KEY))){
                return true;
            }else {
                rep.sendError(403,context.getMessage("fail_bad_requeset",null, LocaleContextHolder.getLocale()));
            }
        } catch (NoSuchAlgorithmException e) {
            rep.sendError(500,context.getMessage("fail_server_error",null, LocaleContextHolder.getLocale()));
        }
        return false;
    }
}
