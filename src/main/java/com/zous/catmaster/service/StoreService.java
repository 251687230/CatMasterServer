package com.zous.catmaster.service;

import com.zous.catmaster.entity.Store;
import com.zous.catmaster.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    StoreMapper storeMapper;

    public List<Store> getStores(String userId){
        return storeMapper.findAllByManagerId(userId);
    }

    public void deleteStore(String id) throws Exception{
        storeMapper.deleteById(id);
    }

    public Store saveStore(Store store) throws Exception{
        storeMapper.save(store);
        return store;
    }

    public Optional<Store> getStore(String storeId){
        return storeMapper.findById(storeId);
    }
}
