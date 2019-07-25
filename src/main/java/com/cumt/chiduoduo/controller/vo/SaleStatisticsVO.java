package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：销售统计展示类
 *
 */

@Data
public class SaleStatisticsVO {
    private Integer productId;
    //商品名字
    private String ProductName;

    private String description;

    private Integer categoryId;

    private String categoryName;

    private BigDecimal price;

    private Integer sales;

    private String imgNo1;
    private Integer stock;
    //商品规格
    private String specification;
}
