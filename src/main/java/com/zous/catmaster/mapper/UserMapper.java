package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserMapper extends CrudRepository<User,String> {
    User findByUserNameAndPassword(String userNames,String password);
}
