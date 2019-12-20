package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Manager;
import org.springframework.data.repository.CrudRepository;

public interface ManagerMapper extends CrudRepository<Manager,String> {
}
