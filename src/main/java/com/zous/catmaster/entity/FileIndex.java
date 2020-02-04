package com.zous.catmaster.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileIndex {
    @Id
    private String id;

    private int fileType;

    private String path;

    public FileIndex(){}

    public FileIndex(String id, String path,int fileType) {
        this.id = id;
        this.path = path;
        this.fileType = fileType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
