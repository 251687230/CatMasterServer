package com.zous.catmaster.entity;


import com.zous.catmaster.CatmasterApplication;
import com.zous.catmaster.mapper.UserMapper;
import org.apache.tomcat.util.security.MD5Encoder;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.ApplicationContextTestUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatmasterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void insertUserTest1(){
        List<User> users = new ArrayList<>();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(Role.ROLE_MASTER,applicationContext.getMessage("role.roleType.master",null, LocaleContextHolder.getLocale())));
        users.add(new User("zhangsan", MD5Encoder.encode("123456".getBytes()),"",roles));
        userMapper.saveAll(users);
    }

    @Test
    public void insertUserTest2(){
        List<User> users = new ArrayList<>();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2,applicationContext.getMessage("role.roleType.master",null, LocaleContextHolder.getLocale())));
        users.add(new User("zhangsan", MD5Encoder.encode("123456".getBytes()),"",roles));
        userMapper.saveAll(users);
    }
}
