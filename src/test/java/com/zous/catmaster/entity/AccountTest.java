package com.zous.catmaster.entity;


import com.zous.catmaster.CatmasterApplication;
import com.zous.catmaster.mapper.AccountMapper;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatmasterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    AccountMapper accountMapper;

    @Test
    public void insertUserTestSuccess(){
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("zhangsan", MD5Encoder.encode("123456".getBytes())));
        accountMapper.saveAll(accounts);
    }

    @Test
    public void insertUserTestFail(){
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("zhangsan", MD5Encoder.encode("123456".getBytes())));
        accounts.add(new Account("zhangsan", MD5Encoder.encode("1234567".getBytes())));
        Assertions.assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                accountMapper.saveAll(accounts);
            }
        });
    }

}
