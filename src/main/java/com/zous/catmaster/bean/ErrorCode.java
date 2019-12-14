package com.zous.catmaster.bean;

public class ErrorCode {
    public static int SUCCESS = 0;

    //Account_LOGIN
    public static int FAIL_ACCOUNT_NOT_EXIST = -1001;
    public static int FAIL_NOT_ACTIVITE = -1002;
    public static int FAIL_EXPIRE_INVALID = -1003;
    public static int FAIL_PASSWORD_ERROR = -1004;
    public static int FAIL_TOKEN_TIMEOUT = -1005;
    public static int FAIL_ACCOUNT_EXIT = -1006;
}
