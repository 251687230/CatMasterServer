package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerMapper extends CrudRepository<Customer,String> {
}
