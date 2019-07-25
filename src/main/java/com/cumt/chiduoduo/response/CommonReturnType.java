package com.cumt.chiduoduo.response;

import lombok.Data;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：请求对应的返回处理结果success或者fail
 *
 */
@Data
public class CommonReturnType {
    //表明请求对应的返回处理结果success或者fail
    private String status;
    //若status=success，则data内反馈前端需要的json数据
    //若status=fail,则data内使用通用的错误码格式
    private Object data;

    //定义一个通用的创建方式
    public static CommonReturnType create(Object result) {
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }
}
