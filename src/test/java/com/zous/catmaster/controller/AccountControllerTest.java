package com.zous.catmaster.controller;

import com.google.gson.Gson;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    private AccountService accountService;

    private final String USER_NAME = "lujie";
    private final String PASSWORD = "123456";



    @Test
    public void testRegister() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/account/register");
        builder.param("UserName",USER_NAME);
        builder.param("Password", PASSWORD);

        addHead(builder);
        MvcResult mvcResult = mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("接口返回结果：" + result);
        Gson gson = new Gson();
        Result result1 = gson.fromJson(result, Result.class);
        // 判断接口返回json中success字段是否为true
        Assert.assertEquals(result1.getErrorCode(), ErrorCode.SUCCESS);

        MvcResult mvcResult2 = mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result2 = mvcResult2.getResponse().getContentAsString();
        System.out.println("接口返回结果：" + result2);
        Result result3 = gson.fromJson(result2, Result.class);
        // 判断接口返回json中success字段是否为true
        Assert.assertEquals(result3.getErrorCode(), ErrorCode.FAIL_ACCOUNT_EXIT);
    }


    @Test
    public void testLogin() throws Exception {
        accountService.createManagerAccount(USER_NAME,SecurityUtils.md5(PASSWORD));
        accountService.activeManagerAccount(USER_NAME,7 * 24 * 3600);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/account/login");
        builder.param("UserName",USER_NAME);
        builder.param("Password", PASSWORD);

        addHead(builder);
        MvcResult mvcResult = mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("接口返回结果：" + result);
        Gson gson = new Gson();
        Result result1 = gson.fromJson(result, Result.class);
        // 判断接口返回json中success字段是否为true
        Assert.assertEquals(result1.getErrorCode(), ErrorCode.SUCCESS);
    }
}
