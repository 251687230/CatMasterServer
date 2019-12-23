package com.zous.catmaster.service;

import com.zous.catmaster.entity.Store;
import com.zous.catmaster.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    StoreMapper storeMapper;

    public List<Store> getStores(String userId){
        return storeMapper.findAllByManagerId(userId);
    }
}
