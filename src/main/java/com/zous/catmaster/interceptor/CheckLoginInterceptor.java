package com.zous.catmaster.interceptor;

import com.google.gson.Gson;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.bean.Token;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.entity.Manager;
import com.zous.catmaster.entity.Teacher;
import com.zous.catmaster.mapper.ManagerMapper;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.service.ManagerService;
import com.zous.catmaster.service.TeacherService;
import com.zous.catmaster.utils.ParameterRequestWrapper;
import com.zous.catmaster.utils.SecurityUtils;
import com.zous.catmaster.utils.TokenUtils;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

public class CheckLoginInterceptor implements HandlerInterceptor {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    AccountService accountService;
    @Autowired
    ManagerService managerService;
    @Autowired
    TeacherService teacherService;

    private Gson gson = new Gson();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CheckLogin methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(CheckLogin.class);
        CheckLogin classAnnotation = ((HandlerMethod) handler).getBean().getClass().getAnnotation(CheckLogin.class);
        boolean going = true;
        if (methodAnnotation != null) {
            boolean userToken = methodAnnotation.userToken();
            String[] requestRoles = methodAnnotation.requestRoles();
            going = handleCheckLogin(request, response,userToken,requestRoles);
        }else  if(classAnnotation != null) {
            boolean userToken = classAnnotation.userToken();
            String[] requestRoles = classAnnotation.requestRoles();
            going = handleCheckLogin(request, response,userToken,requestRoles);
        }

        return going;
    }

    private boolean handleCheckLogin(HttpServletRequest request, HttpServletResponse response,boolean userToken,String[] reqeustRoles) throws IOException, NoSuchAlgorithmException {
        Token token = null;
        boolean isTimeOut = false;
        if(userToken) {
            String tokenStr = request.getHeader("Token");
            TokenUtils tokenUtils = TokenUtils.defaultUtil();
            token = tokenUtils.parse(tokenStr);
            boolean isIllegality = tokenUtils.isIllegality(tokenStr);
            isTimeOut = tokenUtils.isTimeout(token);
            if (isIllegality) {
                response.sendError(Response.SC_FORBIDDEN, applicationContext.getMessage("fail_token_incorret", null, LocaleContextHolder.getLocale()));
                return false;
            }
        }
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
            String userId = "";
            Optional<Account> optionalAccount;
            if(userToken){
                userId = token.getPlayload().getSub();
                optionalAccount = accountService.getAccountByUserId(userId);
            }else {
                String userName = request.getParameter("UserName");
                optionalAccount = accountService.getAccountByUserName(userName);
                if(optionalAccount.isPresent()){
                    userId = optionalAccount.get().getUserId();
                }
            }
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                Set<String> roles = account.getRoleTypes();
                if(!checkPermission(reqeustRoles,roles)){
                    out = response.getWriter();
                    String resultStr = generateResult(ErrorCode.FAIL_NO_PERMISSION);
                    out.append(resultStr);
                }
                if(roles.contains(AppConstant.ROLE_TYPE_MANAGER)) {
                    Optional<Manager> managerOptional = managerService.getManager(account.getUserId());
                    if(!managerOptional.isPresent()){
                        response.sendError(Response.SC_INTERNAL_SERVER_ERROR);
                        return false;
                    }
                    Manager manager = managerOptional.get();
                    long expireTime =  manager.getExpireTime();
                    if (expireTime< 0) {
                        out = response.getWriter();
                        String resultStr = generateResult(ErrorCode.FAIL_NOT_ACTIVITY);
                        out.append(resultStr);
                        return false;
                    }
                    if (expireTime < Calendar.getInstance().getTimeInMillis()) {
                        out = response.getWriter();
                        String resultStr = generateResult(ErrorCode.FAIL_EXPIRE_INVALID);
                        out.append(resultStr);
                        return false;
                    }
                    request.setAttribute("UserId", userId);
                    return true;
                }else if(roles.contains(AppConstant.ROLE_TYPE_TEACHER)){
                    Optional<Teacher> teacherOptional = teacherService.getTeacher(userId);
                    if(!teacherOptional.isPresent()){
                        response.sendError(Response.SC_INTERNAL_SERVER_ERROR);
                        return false;
                    }
                    Teacher teacher = teacherOptional.get();
                    long expireTime = teacher.getManager().getExpireTime();
                    if(expireTime < 0 || expireTime < Calendar.getInstance().getTimeInMillis()){
                        out = response.getWriter();
                        String resultStr = generateResult(ErrorCode.FAIL_MANAGER_FORBID);
                        out.append(resultStr);
                        return false;
                    }
                    request.setAttribute("UserId", userId);
                    return true;
                }
            } else {
                out = response.getWriter();
                String resultStr = generateResult(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
                out.append(resultStr);
                return false;
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        response.sendError(Response.SC_INTERNAL_SERVER_ERROR);
        return false;
    }

    private String generateResult(int code){
        Result result = new Result();
        result.setErrorCode(code);
        if(code == ErrorCode.FAIL_ACCOUNT_NOT_EXIST){
            result.setDescription(applicationContext.getMessage("fail_account_not_exist", null, LocaleContextHolder.getLocale()));
        }else if(code == ErrorCode.FAIL_NOT_ACTIVITY){
            result.setDescription(applicationContext.getMessage("fail_not_active", null, LocaleContextHolder.getLocale()));
        }else if(code == ErrorCode.FAIL_EXPIRE_INVALID){
            result.setDescription(applicationContext.getMessage("fail_expire_invalid", null, LocaleContextHolder.getLocale()));
        }else if(code == ErrorCode.FAIL_MANAGER_FORBID){
            result.setDescription(applicationContext.getMessage("fail_manager_forbid", null, LocaleContextHolder.getLocale()));
        }else if(code == ErrorCode.FAIL_NO_PERMISSION){
            result.setDescription(applicationContext.getMessage("fail_no_permission", null, LocaleContextHolder.getLocale()));
        }
        return gson.toJson(result);
    }

    private boolean checkPermission(String[] requestRoles,Set<String> ownRoles){
        boolean hasPermission = false;
        for(String requestRole : requestRoles){
            hasPermission = ownRoles.contains(requestRole);
            if(hasPermission){
                break;
            }
        }
        return hasPermission;
    }
}
