package com.zous.catmaster.bean;

import com.google.gson.annotations.SerializedName;

public class SmsResult {
    @SerializedName("Message")
    private String message;
    @SerializedName("RequestId")
    private String requestId;
    @SerializedName("BizId")
    private String bizId;
    @SerializedName("Code")
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
