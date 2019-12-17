package com.zous.catmaster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zous.catmaster.CatmasterApplication;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.mapper.AccountMapper;
import com.zous.catmaster.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;


public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    private AccountMapper accountMapper;

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
    }

    @Test
    public void testLogin() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,1);
        accountMapper.save(new Account(USER_NAME,SecurityUtils.md5(PASSWORD),true,calendar.getTimeInMillis()));

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
