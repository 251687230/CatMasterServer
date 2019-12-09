package com.zous.catmaster.service;

import com.zous.catmaster.entity.Account;
import com.zous.catmaster.mapper.AccountMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    AccountMapper accountMapper;

    public Optional<Account> getAccount(String userName){
        return  Optional.ofNullable(accountMapper.findByUserName(userName));
    }

    public int saveAccount(String userName,String password){
        Account account = new Account(userName,password);
        try {
            accountMapper.save(account);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
