package com.cumt.chiduoduo.service.model;

import lombok.Data;

import java.math.BigDecimal;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：心愿单类服务层模型
 *
 */
@Data
public class WishListModel {

  private Integer productId;
  //商品名字
  private String ProductName;

  private String description;

  private BigDecimal price;

  private Integer sales;

  private String imgNo1;

  private Integer categoryId;
//商品类别名称
  private String categotyName;

  private Integer userId;
}
