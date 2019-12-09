package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountMapper extends CrudRepository<Account,String> {
    Account findByUserName(String userName);
}
