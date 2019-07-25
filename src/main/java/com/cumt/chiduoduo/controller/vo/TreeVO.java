package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：树形商品类别展示类
 *
 */

@Data
public class TreeVO {
    private Integer id;

    private String name;

    private String icon;
    private List<CategoryTreeVo> categoryTreeVoList;
}
