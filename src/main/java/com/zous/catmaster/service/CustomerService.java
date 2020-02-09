package com.zous.catmaster.service;

import com.zous.catmaster.entity.AccountStoreKey;
import com.zous.catmaster.entity.Customer;
import com.zous.catmaster.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;

    public void saveCustomer(Customer customer) {
        customerMapper.save(customer);
    }

    public List<Customer> getCustomers(String storeId){
        return customerMapper.findAllByAccountStoreKeyStoreId(storeId);
    }

    public Optional<Customer> getCustomer(AccountStoreKey accountStoreKey){
        return customerMapper.findById(accountStoreKey);
    }

}
