package com.zous.catmaster.interceptor;

import com.google.gson.Gson;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.Token;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.utils.ParameterRequestWrapper;
import com.zous.catmaster.utils.TokenUtils;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Optional;

public class CheckLoginInterceptor implements HandlerInterceptor {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CheckLogin methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(CheckLogin.class);
        CheckLogin classAnnotation = ((HandlerMethod) handler).getBean().getClass().getAnnotation(CheckLogin.class);
        boolean going = true;
        if (classAnnotation != null) {
            going = handleCheckLogin(request, response);
        }

        if (going && methodAnnotation != null) {
            going = handleCheckLogin(request, response);
        }
        return going;
    }

    private boolean handleCheckLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenStr = request.getHeader("Token");
        TokenUtils tokenUtils = TokenUtils.defaultUtil();
        Token token = tokenUtils.parse(tokenStr);
        boolean isIllegality = tokenUtils.isIllegality(tokenStr);
        boolean isTimeOut = tokenUtils.isTimeout(token);
        if (isIllegality) {
            response.sendError(Response.SC_FORBIDDEN, applicationContext.getMessage("fail_token_incorret", null, LocaleContextHolder.getLocale()));
            return false;
        }
        Gson gson = new Gson();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            if (isTimeOut) {
                Result result = new Result(ErrorCode.FAIL_TOKEN_TIMEOUT);
                result.setDescription(applicationContext.getMessage("fail_token_timeout", null, LocaleContextHolder.getLocale()));
                String resultStr = gson.toJson(result);
                out = response.getWriter();
                out.append(resultStr);
                return false;
            }
            long userId = Long.parseLong(token.getPlayload().getSub());
            Optional<Account> optionalAccount = accountService.getAccount(userId);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                if (!account.isActive()) {
                    Result result = new Result(ErrorCode.FAIL_NOT_ACTIVITY);
                    result.setDescription(applicationContext.getMessage("fail_not_active", null, LocaleContextHolder.getLocale()));
                    out = response.getWriter();
                    String resultStr = gson.toJson(result);
                    out.append(resultStr);
                    return false;
                }
                if(account.getExpires() < Calendar.getInstance().getTimeInMillis()){
                    Result result = new Result(ErrorCode.FAIL_EXPIRE_INVALID);
                    result.setDescription(applicationContext.getMessage("fail_expire_invalid", null, LocaleContextHolder.getLocale()));
                    out = response.getWriter();
                    String resultStr = gson.toJson(result);
                    out.append(resultStr);
                    return false;
                }
                ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request);
                parameterRequestWrapper.addParameter("UserId", userId);
                return true;
            } else {
                Result result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
                result.setDescription(applicationContext.getMessage("fail_account_not_exist", null, LocaleContextHolder.getLocale()));
                out = response.getWriter();
                String resultStr = gson.toJson(result);
                out.append(resultStr);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(Response.SC_INTERNAL_SERVER_ERROR);
            return false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
