package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.controller.Form.OrderCommentForm;
import com.cumt.chiduoduo.controller.Form.OrderForm;
import com.cumt.chiduoduo.controller.vo.OrderMasterVO;
import com.cumt.chiduoduo.controller.vo.ProductListVO;
import com.cumt.chiduoduo.controller.vo.ShoppingCartVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.OrderMaster;
import com.cumt.chiduoduo.response.CommonReturnType;
import com.cumt.chiduoduo.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户订单api接口控制类
 *
 */
@Slf4j
@RestController("order")
@RequestMapping("/order")
//defalut_allowed_headers允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
@CrossOrigin(allowCredentials = "true",allowedHeaders =  "*")
@Api(value = "/order", tags = "Order", description = "order")
public class OrderController extends BaseController{
  @Autowired
  private OrderService orderService;

  SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

  /**
   *   //用户点击提交订单并确认订单
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/createorder", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType createOrder(@RequestParam("user_id") Integer userId) throws BusinessException {
    orderService.createOrder(userId);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"*用户编号为："+userId+"的用户点击提交订单并确认订单");
    return CommonReturnType.create(null);
  }

  /**
   * //首页用户点击我要吃，将商品信息添加到购物车中
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/addtoshoppingcart", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType addToShoppingCart(@RequestParam("user_id") Integer userId,
                                            @RequestParam("product_id") Integer productId) throws BusinessException {
    if (userId == null || productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    orderService.addProductToShoppingCart(userId, productId);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"编号为："+userId+"的用户点击我要吃，将商品编号为"+productId+"的商品添加到购物车");
    return CommonReturnType.create(null);
  }

  /**
   *   //购物车页面，如果该商品的数量为零，那么将该用户的购物车中的该数据清除
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deleteshoppingcartitem", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteShoppingCartItem(@RequestParam("user_id") Integer userId,
                                                 @RequestParam("product_id") Integer productId) throws BusinessException {
    if (userId == null || productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    orderService.deleteShoppingCartItem(userId, productId);
    return CommonReturnType.create(null);
  }

  /**
   *  //展示指定用户的购物车数据
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getshoppingcart", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getShoppingCart(@RequestParam("user_id") Integer userId) throws BusinessException {
    if (userId == null ) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<ShoppingCartVO> shoppingCartVOList = orderService.getShoppingCart(userId);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"编号为："+userId+"的用户进入购物车界面，查看其购物车情况");
    return CommonReturnType.create(shoppingCartVOList);
  }

  /**
   *   //用户增加购物车指定商品数量
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/addoneproductamountinshoppingcart", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType addOneProductAmountInShoppingCart(@RequestParam("user_id") Integer userId,
                                                         @RequestParam("product_id") Integer productId) throws BusinessException {
    if (userId == null || productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    boolean result = orderService.addOneProductAmountInShoppingCart(userId, productId);
    if (result == false) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"————"+"编号为"+userId+"的用户，在购物车界面，对编号"+productId+"的商品进行数量+1的操作，但是操作失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"编号为"+userId+"的用户，在购物车界面，成功对编号"+productId+"的商品进行数量+1的操作");
    return CommonReturnType.create(null);
  }

  /**
   *   //用户减少购物车指定商品数量
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/reduceoneproductamountinshoppingcart", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType reduceOneProductAmountInShoppingCart(@RequestParam("user_id") Integer userId,
                                                            @RequestParam("product_id") Integer productId) throws BusinessException {
    if (userId == null || productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    boolean result = orderService.reduceOneProductAmountInShoppingCart(userId, productId);
    if (result == false) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"————"+"编号为"+userId+"的用户，在购物车界面，对编号"+productId+"的商品进行数量-1的操作,但是操作失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"编号为"+userId+"的用户，在购物车界面，成功对编号"+productId+"的商品进行数量-1的操作");
    return CommonReturnType.create(null);
  }

  /**
   * 用户查看订单
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/searchorder", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType searchOrder(@RequestParam("user_id") Integer userId) throws BusinessException {
    if (userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<OrderMasterVO> orderMasterVOList = orderService.searchOrder(userId);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"编号为"+userId+"的用户，进入订单界面，查看历史订单");
    return CommonReturnType.create(orderMasterVOList);
  }

  /**
   * 订单评论
   * @param
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/ordercomment", method = RequestMethod.POST)
  public CommonReturnType orderComment(@RequestParam(value = "totalorderSn") String totalorderSn,
                                       @RequestParam(value = "orderMasterId") Integer orderMasterId,
                                       @RequestParam(value = "quality") Integer quality,
                                       @RequestParam(value = "taste") Integer taste,
                                       @RequestParam(value = "service") Integer service,
                                       @RequestParam(value = "comment") String comment,
                                        MultipartFile img1,
                                        MultipartFile img2,
                                        MultipartFile img3) throws Exception {
    OrderCommentForm orderCommentForm = new OrderCommentForm();
    orderCommentForm.setComment(comment);
    orderCommentForm.setOrderMasterId(orderMasterId);
    orderCommentForm.setQuality(quality);
    orderCommentForm.setService(service);
    orderCommentForm.setTaste(taste);
    orderCommentForm.setOrderMasterId(orderMasterId);
    orderCommentForm.setTotalorderSn(totalorderSn);
    if (img1!=null) {
      orderCommentForm.setImg1(img1);
    }
    if (img2!=null) {
      orderCommentForm.setImg2(img2);
    }
    if (img3!=null) {
      orderCommentForm.setImg3(img3);
    }
    boolean result = orderService.commentOrder(orderCommentForm);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"用户对未评论的商品进行了一次评价。");
    return CommonReturnType.create(null);
  }
}
