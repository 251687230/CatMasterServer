package com.zous.catmaster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zous.catmaster.CatmasterApplication;
import com.zous.catmaster.utils.SecurityUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatmasterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    protected MockMvc mvc;
    protected static String VERIFE_KEY = "zous.catmaster@2019";
    protected final String USER_NAME = "lujie";
    protected final String PASSWORD = "123456";
    Gson gson = new Gson();

    @Autowired
    protected WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                //.apply(springSecurity())
                .build();
    }

    protected void addHead(MockHttpServletRequestBuilder builder) throws NoSuchAlgorithmException {
        String random = UUID.randomUUID().toString();
        String timeStamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
        builder.header("Random", random);
        builder.header("Timestamp",timeStamp);
        builder.header("EncoderStr", SecurityUtils.md5(timeStamp + random + VERIFE_KEY));
    }
}
