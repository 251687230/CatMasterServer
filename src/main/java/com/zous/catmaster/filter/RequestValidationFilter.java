package com.zous.catmaster.filter;

import com.zous.catmaster.utils.SecurityUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 拦截所有请求，验证请求是否为合法的请求
 */
//TODO 将来再考虑重放攻击
@WebFilter(urlPatterns="/**",filterName="requestValidationFilter")
public class RequestValidationFilter implements Filter {
    @Autowired
    ApplicationContext context;

    private static String VERIFE_KEY = "zous.catmaster@2019";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rep = (HttpServletResponse)response;
        String encoderStr = req.getHeader("EncoderStr");
        String timeStamp = req.getHeader("UUID");
        String random = req.getHeader("Random");
        try {
            if(encoderStr != null && encoderStr.equalsIgnoreCase(SecurityUtils.md5(timeStamp + random + VERIFE_KEY))){
                chain.doFilter(request, response);
            }else {
                rep.sendError(403,context.getMessage("fail_bad_requeset",null, LocaleContextHolder.getLocale()));
            }
        } catch (NoSuchAlgorithmException e) {
            rep.sendError(500,context.getMessage("fail_server_error",null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void destroy() {

    }
}
