package com.zous.catmaster.service;

import com.zous.catmaster.entity.Manager;
import com.zous.catmaster.mapper.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    public Optional<Manager> getManager(String userId){
        return managerMapper.findById(userId);
    }
}
