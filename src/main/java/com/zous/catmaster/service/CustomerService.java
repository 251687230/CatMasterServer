package com.zous.catmaster.service;

import com.zous.catmaster.entity.Customer;
import com.zous.catmaster.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;

    public Customer saveCustomer(Customer customer){
        customerMapper.save(customer);
        return customer;
    }

    public Optional<Customer> getCustomer(String id){
        return  customerMapper.findById(id);
    }
}
