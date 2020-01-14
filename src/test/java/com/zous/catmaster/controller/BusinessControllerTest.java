package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Store;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.utils.SecurityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class BusinessControllerTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountController accountController;
    String token= null;

    @Before
    public void init() throws NoSuchAlgorithmException, JsonProcessingException {
        //注册及登录
        accountService.createManagerAccount(USER_NAME, SecurityUtils.md5(PASSWORD));
        accountService.activeManagerAccount(USER_NAME,7 * 24 * 3600);

        Result resultObj = accountController.login(USER_NAME,PASSWORD);
        token = (String) gson.fromJson(resultObj.getData(), HashMap.class).get("sessionToken");
    }

    @Test
    public void testGetStores() throws Exception {
        Assert.assertNotNull(token);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/business/getStores");
        builder.header("Token",token);

        addHead(builder);
        MvcResult mvcResult = mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("接口返回结果：" + result);
        Result result1 = gson.fromJson(result, Result.class);
        // 判断接口返回json中success字段是否为true
        Assert.assertEquals(result1.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void testSaveStores() throws Exception {
        Assert.assertNotNull(token);

        Store store = new Store();
        store.setName("测试名称");
        store.setAreaCode("123456");
        store.setDetailAddr("111223");

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/business/saveStore");
        builder.header("Token",token);
        builder.header("Content-Type","application/json;charset=UTF-8");
        builder.content(gson.toJson(store));

        addHead(builder);
        MvcResult mvcResult = mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200)).andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("接口返回结果：" + result);
        Result result1 = gson.fromJson(result, Result.class);
        // 判断接口返回json中success字段是否为true
        Assert.assertEquals(result1.getErrorCode(), ErrorCode.SUCCESS);
    }
}
