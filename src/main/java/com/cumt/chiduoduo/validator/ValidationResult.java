package com.cumt.chiduoduo.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：数据校验类
 *
 */
@Data
public class ValidationResult {
    //校验结果是否有错
    private boolean hasErrors=false;
    //存放错误信息的map
    private Map<String, String> errorMsgMap=new HashMap<>();

    //实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrMsg() {
        return StringUtils.join(errorMsgMap.values().toArray(), ",");
    }
}
