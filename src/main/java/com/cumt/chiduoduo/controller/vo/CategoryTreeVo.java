package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：展示二级树形商品列表
 *
 */

@Data
public class CategoryTreeVo {
    private Integer id;

    private String name;

    private String imgurl;

    private String icon;
    private Integer highestCategoryId;

    private List<ProductListVO> productListVOList;
}
