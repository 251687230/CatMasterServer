package com.zous.catmaster.service;

import com.zous.catmaster.entity.Account;
import com.zous.catmaster.mapper.AccountMapper;
import com.zous.catmaster.utils.SecurityUtils;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    AccountMapper accountMapper;

    public Optional<Account> getAccount(String userName){
        return  Optional.ofNullable(accountMapper.findByUserName(userName));
    }

    public Optional<Account> getAccount(long userId){
        return Optional.ofNullable(accountMapper.findById(userId));
    }

    public int save(Account account){
        try {
            accountMapper.save(account);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return 0;
    }


    public int saveAccount(String userName,String password){
        Account account = new Account(userName,password);
        return save(account);
    }
}
