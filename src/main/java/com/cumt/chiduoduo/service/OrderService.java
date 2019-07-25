package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.controller.Form.OrderCommentForm;
import com.cumt.chiduoduo.controller.Form.OrderForm;
import com.cumt.chiduoduo.controller.vo.OrderMasterVO;
import com.cumt.chiduoduo.controller.vo.ShoppingCartVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.OrderMaster;
import com.cumt.chiduoduo.service.model.OrderModel;
import io.swagger.models.auth.In;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：订单服务接口
 *
 */
public interface OrderService {
   boolean createOrder(Integer userId) throws BusinessException;

  void addProductToShoppingCart(Integer userId, Integer productId) throws BusinessException;
  //前台中的购物车页面，如果该商品的数量为零，那么将该用户的购物车中的该数据清除
  void deleteShoppingCartItem(Integer userId, Integer productId);

  //展示指定用户的购物车数据
  List<ShoppingCartVO> getShoppingCart(Integer userId) throws BusinessException;

  //用户增加购物车指定商品数量
  boolean addOneProductAmountInShoppingCart(Integer userId, Integer productId) throws BusinessException;
  //用户减少购物车指定商品数量
  boolean reduceOneProductAmountInShoppingCart(Integer userId, Integer productId) throws BusinessException;

  List<OrderMasterVO> searchOrder(Integer userId) throws BusinessException;
  //创建订单评论
  boolean commentOrder(OrderCommentForm orderCommentForm) throws Exception;
}
