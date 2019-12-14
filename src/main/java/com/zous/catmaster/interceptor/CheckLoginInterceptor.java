package com.zous.catmaster.interceptor;

import com.google.gson.Gson;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.Token;
import com.zous.catmaster.utils.TokenUtils;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckLoginInterceptor implements HandlerInterceptor {
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenStr = request.getHeader("token");
        TokenUtils tokenUtils = TokenUtils.defaultUtil();
        Token token = tokenUtils.parse(tokenStr);
        boolean isIllegality = tokenUtils.isIllegality(tokenStr);
        boolean isTimeOut = tokenUtils.isTimeout(token);
        if(isIllegality) {
            response.sendError(Response.SC_FORBIDDEN, applicationContext.getMessage("fail_token_incorret", null, LocaleContextHolder.getLocale()));
            return false;
        }
        if(isTimeOut){
            Result result = new Result(ErrorCode.FAIL_TOKEN_TIMEOUT);
            result.setDescription(applicationContext.getMessage("fail_token_timeout",null,LocaleContextHolder.getLocale()));
            Gson gson = new Gson();
            String resultStr = gson.toJson(result);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(resultStr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return false;
        }
        return true;
    }
}
