package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.AccountStoreKey;
import com.zous.catmaster.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerMapper extends CrudRepository<Customer, AccountStoreKey> {
    List<Customer> findAllByAccountStoreKeyStoreId(String storeId);
}
