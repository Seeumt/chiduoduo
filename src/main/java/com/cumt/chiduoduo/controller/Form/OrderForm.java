package com.cumt.chiduoduo.controller.Form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：创建订单表单
 *
 */

@Data
public class OrderForm {
    @NotBlank(message = "用户id必填")
    private Integer userId;
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
