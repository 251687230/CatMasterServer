package com.zous.catmaster.mapper;

import com.zous.catmaster.entity.FileIndex;
import org.springframework.data.repository.CrudRepository;

public interface FileMapper extends CrudRepository<FileIndex,String> {
}
