package com.zous.catmaster.service;

import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.entity.Account;
import com.zous.catmaster.entity.Manager;
import com.zous.catmaster.mapper.AccountMapper;
import com.zous.catmaster.mapper.ManagerMapper;
import com.zous.catmaster.utils.SecurityUtils;
import jdk.nashorn.internal.runtime.options.Option;
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

    public Optional<Account> getAccount(String id){
        return  accountMapper.findById(id);
    }

    public Optional<Account> getAccountByUserName(String userName){
        return accountMapper.findByUserName(userName);
    }


    public void activeManagerAccount(String username,long expireDuration){
        Optional<Account> accountOptional =  accountMapper.findByUserName(username);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Optional<Manager> managerOptional = managerMapper.findById(account.getId());
            if(managerOptional.isPresent()){
                Manager manager = managerOptional.get();
                manager.setExpireTime(Calendar.getInstance().getTimeInMillis() + expireDuration);
            }
        }
    }

    public Account saveAccount(Account account){
        accountMapper.save(account);
        return account;
    }

    @Transactional
    public void createManagerAccount(String userName, String password) {
        Set<String> roleTypes = new HashSet<>();
        roleTypes.add(AppConstant.ROLE_TYPE_MANAGER);
        Calendar calendar = Calendar.getInstance();
        Account account = new Account(userName, password);
        account.setRoleTypes(roleTypes);
        account.setCreateTime(calendar.getTimeInMillis());
        accountMapper.save(account);
        Manager manager = new Manager();
        manager.setId(account.getId());

        calendar.add(Calendar.DAY_OF_MONTH,3);
        manager.setExpireTime(calendar.getTimeInMillis()
        );

        managerMapper.save(manager);
    }
}
