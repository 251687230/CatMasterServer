package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StoreMapper extends CrudRepository<Store,String> {
    List<Store> findAllByManagerId(String userId);
}
