package com.cumt.chiduoduo.service.model;

import lombok.Data;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：购物车类服务层模型
 *
 */
@Data
public class ShoppingCartModel {
  private Integer productId;

  private Integer amount;

  public ShoppingCartModel(Integer productId, Integer amount) {
    this.productId = productId;
    this.amount = amount;
  }
}
