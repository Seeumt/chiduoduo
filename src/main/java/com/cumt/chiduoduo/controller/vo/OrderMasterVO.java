package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：小订单数据展示类
 *
 */

@Data
public class OrderMasterVO {
    private Integer id;

    private String totalorderSn;

    private Integer userId;

    private Integer productId;

    private Integer amount;

    private BigDecimal orderPrice;

    private String  commentTime;

    //商品名字
    private String ProductName;

    private String description;

    private Integer categoryId;

    private String categoryName;
    private BigDecimal price;
    private String imgNo1;
    //商品规格
    private String specification;
    private String comment;
    private List<OrderCommentVO> orderCommentVOList;
}
