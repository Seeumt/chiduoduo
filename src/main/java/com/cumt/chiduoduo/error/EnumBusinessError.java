package com.cumt.chiduoduo.error;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：报错信息返回数据类
 *
 */
public enum EnumBusinessError implements CommonError {
    //通用错误类型
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    UNKNOWN_ERROR(10002, "未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户手机或密码错误"),
    USER_NOT_LOGIN(20003, "用户还未登录"),

    //30000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30001, "库存不足"),
    ;

    private int errCode;
    private String errMsg;

    EnumBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg=errMsg;
        return this;
    }
}
