package com.zous.catmaster.service;

import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.entity.FileIndex;
import com.zous.catmaster.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileService {
    @Autowired
    FileMapper fileMapper;

    public void createFileIndex(String id,String path,int fileType){
       fileMapper.save(new FileIndex(id,path,fileType));
    }

    public String getFilePath(String id){
        Optional<FileIndex> fileIndexOptional = fileMapper.findById(id);
        return fileIndexOptional.map(fileIndex -> AppConstant.IMAGE_STORAGE_PATH + fileIndex.getPath()).orElse("");
    }
}
