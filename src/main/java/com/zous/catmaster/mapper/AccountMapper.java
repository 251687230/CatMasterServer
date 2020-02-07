package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountMapper extends CrudRepository<Account,String> {
    Optional<Account> findByUserName(String userName);
}
