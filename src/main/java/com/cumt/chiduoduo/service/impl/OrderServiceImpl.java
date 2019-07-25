package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.Form.OrderCommentForm;
import com.cumt.chiduoduo.controller.Form.OrderForm;
import com.cumt.chiduoduo.controller.vo.DomitoryProductsVO2;
import com.cumt.chiduoduo.controller.vo.OrderMasterVO;
import com.cumt.chiduoduo.controller.vo.ProductListVO;
import com.cumt.chiduoduo.controller.vo.ShoppingCartVO;
import com.cumt.chiduoduo.convertor.Convertor;
import com.cumt.chiduoduo.dao.*;
import com.cumt.chiduoduo.enums.PayStatusEnum;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.service.OrderService;
import com.cumt.chiduoduo.service.ProductService;
import com.cumt.chiduoduo.service.model.OrderModel;
import com.cumt.chiduoduo.service.model.ProductListModel;
import com.cumt.chiduoduo.service.model.ShoppingCartModel;
import com.cumt.chiduoduo.utils.COSClientUtil;
import com.cumt.chiduoduo.utils.KeyUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：订单服务层实现类
 *
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
  @Resource
  private UserMapper userMapper;
  @Resource
  private ProductMapper productMapper;
  @Resource
  private OrderMasterMapper orderMasterMapper;
  @Resource
  private TotalOrderMapper totalOrderMapper;
  @Autowired
  private ProductService productService;
  @Resource
  private ShoppingCartMapper shoppingCartMapper;
  @Resource
  private CategoryMapper categoryMapper;
  @Resource
  private DomitoryStockMapper domitoryStockMapper;
  @Resource
  private DomitoryMapper domitoryMapper;
  @Autowired
  private SendEmailService sendEmailService;
  @Resource
  private OrderCommentMapper orderCommentMapper;

  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * 创建订单
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean createOrder(Integer userId) throws BusinessException {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户不存在");
    }
    List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserId(userId);

    TotalOrder totalOrder = new TotalOrder();
    totalOrder.setCreateTime(new Date());
    totalOrder.setOrderSn(KeyUtil.genUniqueKey());
    totalOrder.setUserId(userId);
    //TODO 写死订单更新时间，因为没有做支付功能
    totalOrder.setUpdateTime(new Date());
    totalOrder.setUserDomitoryno(user.getDomitoryNo());
    //TODO 我在model的TotalOrder类中，把支付状态写死了
    //计算总价
    BigDecimal totalAmount= BigDecimal.valueOf(0.0);
    //创建购物车小车列表
    List<ShoppingCartModel> shoppingCartModelList = new ArrayList<>();
    for (ShoppingCart shoppingCart : shoppingCartList) {
      Product product = productMapper.selectByPrimaryKey(shoppingCart.getProductId());
      BigDecimal smallOrderTotalPrice=product.getPrice().multiply(new BigDecimal(shoppingCart.getAmount()));
      //创建小订单
      OrderMaster orderMaster = new OrderMaster();
      orderMaster.setAmount(shoppingCart.getAmount());
      orderMaster.setOrderPrice(smallOrderTotalPrice);
      orderMaster.setProductId(shoppingCart.getProductId());
      orderMaster.setTotalorderSn(totalOrder.getOrderSn());
      orderMaster.setUserId(userId);
      //小订单入库
      orderMasterMapper.insertSelective(orderMaster);
      totalAmount=totalAmount.add(smallOrderTotalPrice);
      //加完一个总价，删一个购物车
      shoppingCartMapper.deleteByPrimaryKey(shoppingCart.getId());
      //将创建的宿舍库存小车装入列表中
      ShoppingCartModel shoppingCartModel = new ShoppingCartModel(shoppingCart.getProductId(),shoppingCart.getAmount());
      shoppingCartModelList.add(shoppingCartModel);
    }
    totalOrder.setTotalPrice(totalAmount);

    //将TotalOrder入库
    totalOrderMapper.insertSelective(totalOrder);

    //扣宿舍库存
    Domitory domitory = domitoryMapper.selectByDomitoryNoAndBuildingId(user.getDomitoryNo(), user.getBuildingId());

    productService.decreaseDomitoryStock(shoppingCartModelList,domitory.getId());

    //商品总表加销量
    //TODO 因为管理员配零食的功能没做，所以直接在这里扣商品总表库存
    productService.increaseSales(shoppingCartModelList);

    //给用户发邮件，通知一下啦
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = format.format(new Date().getTime());
    sendEmailService.send(user.getEmail(), "", "【吃哆哆订单】",
      "您于 " + time + " 在吃哆哆上，消费订单号为：" + totalOrder.getOrderSn() + "，总价计：" + totalAmount + "元的订单，请您尽快完成支付！");
    log.warn("用户"+userId+"在吃哆哆上下单成功，已经为他发送通知邮件，邮件地址为："+user.getEmail());
    return true;
  }

  /**
   *   //从首页点我要吃后，将商品添加到购物车表
   * @param userId
   * @param productId
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void addProductToShoppingCart(Integer userId, Integer productId) throws BusinessException {
    ShoppingCart shoppingCart=shoppingCartMapper.selectByUserIdAndProductId(userId, productId);
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户不存在");
    }
    Domitory domitory = domitoryMapper.selectByDomitoryNoAndBuildingId(user.getDomitoryNo(), user.getBuildingId());
    List<DomitoryStock> domitoryStockList = domitoryStockMapper.selectByDomitoryId(domitory.getId());
    int domitoryAmount=0;
    for (DomitoryStock domitoryStock : domitoryStockList) {
      if (domitoryStock.getProductId().equals(productId)) {
        domitoryAmount = domitoryStock.getDomitoryStock();
      }
    }
    if (shoppingCart == null) {
      ShoppingCart shoppingCart1 = new ShoppingCart();
      shoppingCart1.setProductId(productId);
      shoppingCart1.setUserId(userId);
      //这里把数量写死为1
      shoppingCart1.setAmount(1);
      shoppingCartMapper.insertSelective(shoppingCart1);
    }else {
      ShoppingCart shoppingCart1 = new ShoppingCart();
      BeanUtils.copyProperties(shoppingCart,shoppingCart1);
      if (shoppingCart1.getAmount() < domitoryAmount) {
        shoppingCart1.setAmount(shoppingCart.getAmount()+1);
        shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart1);
      }else {
        String time = format.format(new Date().getTime());
        log.warn("在"+time+"有用户增加购物车商品数量等于商品库存时，继续添加，添加操作失败");
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "宿舍库存不足");
      }
    }
  }

  /**
   *
   *   //前台中的购物车页面，如果该商品的数量为零，那么将该用户的购物车中的该数据清除
   * @param userId
   * @param productId
   */
  @Override
  @Transactional
  public void deleteShoppingCartItem(Integer userId, Integer productId) {
    ShoppingCart shoppingCart = shoppingCartMapper.selectByUserIdAndProductId(userId, productId);
    shoppingCartMapper.deleteByPrimaryKey(shoppingCart.getId());
  }

  /**
   * 展示指定用户的购物车数据
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public List<ShoppingCartVO> getShoppingCart(Integer userId) throws BusinessException {
    List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserId(userId);
    if (shoppingCartList == null) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"————"+"编号为："+userId+"的用户点进入空购物车");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "无该用户购物车数据");
    }
    List<Product> productList = new ArrayList<>();
    for (ShoppingCart shoppingCart : shoppingCartList) {
      Product product = productMapper.selectByPrimaryKey(shoppingCart.getProductId());
      productList.add(product);
    }
    if (productList.isEmpty()) {
      throw new BusinessException(EnumBusinessError.UNKNOWN_ERROR, "该用户无购物车数据");
    }
    List<ProductListModel> productListModelList = ProductDOList2ProductModelListConver(productList);
    List<ProductListVO> productListVOList = new Convertor().productListModelList2ProductListVOListConvert(productListModelList);
    List<ShoppingCartVO> shoppingCartVOList = new ArrayList<>();
    for (ProductListVO productListVO : productListVOList) {
      ShoppingCartVO shoppingCartVO = new ShoppingCartVO();
      BeanUtils.copyProperties(productListVO,shoppingCartVO);
      for (ShoppingCart shoppingCart : shoppingCartList) {
        if (shoppingCart.getProductId().equals(shoppingCartVO.getProductId())) {
          shoppingCartVO.setAmount(shoppingCart.getAmount());
//          String time = format.format(new Date().getTime());
//          log.warn("在"+time+"————"+"编号为："+userId+"的用户结算购物车");
        }
      }
      shoppingCartVOList.add(shoppingCartVO);

      //获取宿舍内零食列表
      List<DomitoryProductsVO2> domitoryProductsVO2List = productService.getDomitoryProductsList(userId);
      for (DomitoryProductsVO2 domitoryProductsVO2 : domitoryProductsVO2List) {
        if (domitoryProductsVO2.getProductId().equals(shoppingCartVO.getProductId())) {
          shoppingCartVO.setDomitoryStock(domitoryProductsVO2.getDomitoryStock());
          String time = format.format(new Date().getTime());
          log.warn("在"+time+"————"+"编号为："+userId+"的用户点获取购物车零食列表");
        }
      }
    }
    return shoppingCartVOList;
  }

  /**
   *添加一个商品到购物车
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean addOneProductAmountInShoppingCart(Integer userId, Integer productId) throws BusinessException {
    List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserId(userId);
    if (shoppingCartList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "您的购物车无数据");
    }
    //TODO 由于防止用户增加的数量要小于宿舍库存的功能繁琐，暂时先不写
    int count=0;
    for (ShoppingCart shoppingCart : shoppingCartList) {
      if (shoppingCart.getProductId().equals(productId)) {
        ShoppingCart shoppingCart1=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCart,shoppingCart1);
        shoppingCart1.setAmount(shoppingCart1.getAmount()+1);
        shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart1);
        count++;
      }
    }
    if (count>1||count==0) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "增加购物车商品数量失败");
    }
    return true;
  }

  /**
   * 扣减一个商品到购物车
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean reduceOneProductAmountInShoppingCart(Integer userId, Integer productId) throws BusinessException {
    List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectByUserId(userId);
    if (shoppingCartList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "您的购物车无数据");
    }
    int count=0;
    for (ShoppingCart shoppingCart : shoppingCartList) {
      if (shoppingCart.getProductId().equals(productId)) {
        ShoppingCart shoppingCart1=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCart,shoppingCart1);
        shoppingCart1.setAmount(shoppingCart1.getAmount()-1);
        if (shoppingCart1.getAmount() == 0) {
          shoppingCartMapper.deleteByPrimaryKey(shoppingCart1.getId());
          String time = format.format(new Date().getTime());
          log.warn("在"+time+"————"+"编号为："+userId+"的用户将购物车内的商品编号为"+productId+"的商品数量减小到0，该零食从购物车列表删去");
        }
        shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart1);
        count++;
      }
    }
    if (count>1||count==0) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"————"+"增加购物车商品数量失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "增加购物车商品数量失败");
    }
    return true;
  }

  /**
   *
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<OrderMasterVO> searchOrder(Integer userId) throws BusinessException {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
    }
    List<OrderMaster> orderMasterList=orderMasterMapper.selectByUserId(userId);
    if (orderMasterList.size() == 0) {
      return null;
    }
    List<OrderMasterVO> orderMasterVOList = new ArrayList<>();
    for (OrderMaster orderMaster : orderMasterList) {
      Product product = productMapper.selectByPrimaryKey(orderMaster.getProductId());
      ProductListModel productListModel = ProductDO2ProductModel(product);
      OrderMasterVO orderMasterVO = new OrderMasterVO();
      BeanUtils.copyProperties(productListModel,orderMasterVO);
      BeanUtils.copyProperties(orderMaster, orderMasterVO);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String time = format.format(orderMaster.getCommentTime().getTime());
      orderMasterVO.setCommentTime(time);
      orderMasterVOList.add(orderMasterVO);
    }
    Collections.reverse(orderMasterVOList);
    return orderMasterVOList;
  }

  @Override
  @Transactional
  public boolean commentOrder(OrderCommentForm orderCommentForm) throws Exception {
    OrderMaster orderMaster = orderMasterMapper.selectByPrimaryKey(orderCommentForm.getOrderMasterId());
    if (orderMaster == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该小订单不存在");
    }
    OrderComment orderComment = new OrderComment();
    BeanUtils.copyProperties(orderCommentForm, orderComment);
    orderComment.setCreateTime(new Date());
    orderComment.setOrderSn(orderCommentForm.getTotalorderSn());

    if (orderCommentForm.getImg1()!=null) {
      COSClientUtil cosClientUtil = new COSClientUtil();
      String url = cosClientUtil.uploadFile2Cos(orderCommentForm.getImg1());
      String imgUrl = cosClientUtil.getImgUrl(url);
      String[] split1 =imgUrl.split("\\?");
      orderComment.setImg1(split1[0]);
    }
    if (orderCommentForm.getImg2()!=null) {
      COSClientUtil cosClientUtil = new COSClientUtil();
      String url = cosClientUtil.uploadFile2Cos(orderCommentForm.getImg2());
      String imgUrl = cosClientUtil.getImgUrl(url);
      String[] split1 =imgUrl.split("\\?");
      orderComment.setImg2(split1[0]);
    }
    if (orderCommentForm.getImg3()!=null) {
      COSClientUtil cosClientUtil = new COSClientUtil();
      String url = cosClientUtil.uploadFile2Cos(orderCommentForm.getImg3());
      String imgUrl = cosClientUtil.getImgUrl(url);
      String[] split1 =imgUrl.split("\\?");
      orderComment.setImg3(split1[0]);
    }


    try {
      orderCommentMapper.insertSelective(orderComment);
    } catch (DuplicateKeyException e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "您已评论，请勿重复评论");
    }

    //写上小订单里的评论
    OrderMaster orderMaster1 = new OrderMaster();
    BeanUtils.copyProperties(orderMaster, orderMaster1);
    orderMaster1.setComment(orderCommentForm.getComment());
    orderMasterMapper.updateByPrimaryKeySelective(orderMaster1);
    return true;
  }
/*
转化方法*/
  //将orderForm转化成orderMaster,同时将orderMaster入库

  private OrderModel orderForm2OrderModelConvert(OrderForm orderForm) throws BusinessException {
    Gson gson = new Gson();
    User user = userMapper.selectByPrimaryKey(orderForm.getUserId());
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "创建订单的用户不存在");
    }
    OrderModel orderModel = new OrderModel();
    orderModel.setCreateTime(new Date());
    orderModel.setOrderSn(KeyUtil.genUniqueKey());
    orderModel.setPayStatus(PayStatusEnum.WAIT.getCode());
    orderModel.setUserDomitoryno(user.getDomitoryNo());
    List<OrderMaster> orderMasterList = new ArrayList<>();
    try {
      orderMasterList = gson.fromJson(orderForm.getItems(),
        new TypeToken<List<OrderMaster>>() {
        }.getType());
    } catch (Exception e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "对象转化错误");
    }
    double totalPrice=0.0;
    for (OrderMaster orderMaster : orderMasterList) {
      orderMaster.setUserId(user.getId());
      Product product = productMapper.selectByPrimaryKey(orderMaster.getProductId());
      orderMaster.setOrderPrice(product.getPrice().multiply(BigDecimal.valueOf(orderMaster.getAmount())));
      orderMaster.setTotalorderSn(orderModel.getOrderSn());
      //orderMaster入库
      totalPrice += orderMaster.getOrderPrice().doubleValue();
      orderMasterMapper.insertSelective(orderMaster);
    }
    orderModel.setOrderMasterList(orderMasterList);
    return orderModel;
  }
  public ProductListModel ProductDO2ProductModel(Product product) {
    ProductListModel productListModel = new ProductListModel();
    BeanUtils.copyProperties(product,productListModel);
    productListModel.setProductId(product.getId());
    productListModel.setProductName(product.getName());
    int categoryId = product.getCategoryId();
    Category category = categoryMapper.selectById(categoryId);
    BeanUtils.copyProperties(category, productListModel);
    productListModel.setCategoryName(category.getName());
    String[] getSpecification = product.getDescription().split("，");
    productListModel.setSpecification(getSpecification[0]);
    return productListModel;
  }

  public List<ProductListModel> ProductDOList2ProductModelListConver(List<Product> productList) {
    return productList.stream().map(e ->
      ProductDO2ProductModel(e)
    ).collect(Collectors.toList());
  }
}

