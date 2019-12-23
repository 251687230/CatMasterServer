package com.zous.catmaster.service;

import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.entity.Manager;
import com.zous.catmaster.mapper.AccountMapper;
import com.zous.catmaster.mapper.ManagerMapper;
import com.zous.catmaster.utils.SecurityUtils;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AccountService {
    Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ManagerMapper managerMapper;

    public Optional<Account> getAccountByUserId(String userId){
        return  accountMapper.findByUserId(userId);
    }


    public Optional<Account> getAccount(long userId){
        return accountMapper.findById(userId);
    }

    public Optional<Account> getAccountByUserName(String userName){
        return accountMapper.findByUserName(userName);
    }


    public void activeManagerAccount(String username,long expireDuration){
        Optional<Account> accountOptional =  accountMapper.findByUserName(username);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Optional<Manager> managerOptional = managerMapper.findById(account.getUserId());
            if(managerOptional.isPresent()){
                Manager manager = managerOptional.get();
                manager.setExpireTime(Calendar.getInstance().getTimeInMillis() + expireDuration);
            }
        }
    }

    @Transactional
    public int createManagerAccount(String userName,String password){
        int result = 0;
        try {
            Set<String> roleTypes = new HashSet<>();
            roleTypes.add(AppConstant.ROLE_TYPE_MANAGER);
            Account account = new Account(userName, password);
            account.setRoleTypes(roleTypes);
            String userId = UUID.randomUUID().toString();
            account.setUserId(userId);
            Manager manager = new Manager();
            manager.setId(userId);
            accountMapper.save(account);
            managerMapper.save(manager);
        }catch (Exception e){
            log.error("创建管理员账号出错",e);
            result = -1;
        }
        return result;
    }
}
