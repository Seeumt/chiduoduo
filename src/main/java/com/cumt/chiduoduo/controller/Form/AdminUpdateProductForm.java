package com.cumt.chiduoduo.controller.Form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：管理员修改商品表单
 *
 */

@Data
public class AdminUpdateProductForm {
    @NotNull(message = "商品id不能为空")
    private Integer id;
    @NotEmpty(message = "商品名称不能为空")
    private String name;
    @NotEmpty(message = "商品描述不能为空")
    private String description;
    @NotEmpty(message = "商品价格不能为空")
    private BigDecimal price;
    @NotEmpty(message = "商品库存不能为空")
    private Integer stock;
    @NotEmpty(message = "商品第一张图不能为空")
    private String imgNo1;
    @NotEmpty(message = "商品第二张图不能为空")
    private String imgNo2;
    @NotEmpty(message = "商品第三张图不能为空")
    private String imgNo3;
}
