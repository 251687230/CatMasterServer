package com.zous.catmaster.bean;

public class Result {
    private int errorCode;
    private String description = "";
    private String data;

    public Result(){}

    public Result(int errorCode){
        this.errorCode = errorCode;
    }

    public Result(int errorCode, String description, String data) {
        this.errorCode = errorCode;
        this.description = description;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
