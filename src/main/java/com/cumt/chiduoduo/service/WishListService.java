package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.service.model.WishListModel;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：心愿单服务接口
 *
 */
public interface WishListService {
  //添加商品到心愿单

  void addProduct(WishListModel wishListModel);
}
