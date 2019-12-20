package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountMapper extends CrudRepository<Account,Long> {
    Optional<Account> findByUserName(String userName);
}
