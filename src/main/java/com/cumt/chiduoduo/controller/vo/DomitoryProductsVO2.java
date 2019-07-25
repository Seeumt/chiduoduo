package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：第二个宿舍商品展示类
 *
 */

@Data
public class DomitoryProductsVO2 {
    private Integer productId;
    //商品名字
    private String ProductName;

    private String description;

    private Integer categoryId;

    private String categoryName;

    private BigDecimal price;

    private Integer domitoryStock;

    private String imgNo1;
    private Integer upOrDown=1;
}
