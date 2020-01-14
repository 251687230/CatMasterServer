package com.zous.catmaster.bean;

public class ErrorCode {
    public static int SUCCESS = 0;
    public static int FAIL_PARAMS_ERROR = -1;

    //Account
    public static int FAIL_ACCOUNT_NOT_EXIST = -1001;
    public static int FAIL_NOT_ACTIVITY = -1002;
    public static int FAIL_EXPIRE_INVALID = -1003;
    public static int FAIL_PASSWORD_ERROR = -1004;
    public static int FAIL_TOKEN_TIMEOUT = -1005;
    public static int FAIL_ACCOUNT_EXIT = -1006;
    public static int FAIL_MANAGER_FORBID = -1007;
    public static int FAIL_NO_PERMISSION = -1008;

    //Captcha
    public static int FAIL_INVALID_PHONENUM = -1101;
    public static int FAIL_CAPTCHA_ERROR = -1102;
    public static int FAIL_CAPTCHA_TIMEOUT = -1103;
    public static int FAIL_GET_CAPTCHA_EXCEPTION = -1104;

    //Store
    public static int FAIL_STORE_NOT_EXIST = -1201;
}
