package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.Form.AdminAddProductForm;
import com.cumt.chiduoduo.controller.Form.AdminUpdateProductForm;
import com.cumt.chiduoduo.controller.vo.*;
import com.cumt.chiduoduo.convertor.Convertor;
import com.cumt.chiduoduo.dao.*;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.service.ProductService;
import com.cumt.chiduoduo.service.model.CommentModel;
import com.cumt.chiduoduo.service.model.ProductListModel;
import com.cumt.chiduoduo.service.model.ShoppingCartModel;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：产品服务层实现类
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

  @Resource
  private CategoryMapper categoryMapper;
  @Resource
  private ProductMapper productMapper;
  @Resource
  private OrderMasterMapper orderMasterMapper;
  @Resource
  private UserMapper userMapper;
  @Resource
  private WishListMapper wishListMapper;
  @Resource
  private DomitoryStockMapper domitoryStockMapper;
  @Resource
  private DomitoryMapper domitoryMapper;
  @Resource
  private TotalOrderMapper totalOrderMapper;
  @Resource
  private OrderCommentMapper orderCommentMapper;
//  @Autowired
//  private OrderRepository orderRepository;

  Convertor convertor = new Convertor();

  /**
   * 获取商品类别信息
   * @return
   * @throws BusinessException
   */
  @Override
  public List<Category> selectAllCategorys() throws BusinessException {
    List<Category> categoryList = categoryMapper.selectAllCategorys();
    if (categoryList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "无法获取商品类别信息");
    }
    return categoryList;
  }

  /**
   * 找到识别该商品类别
   * @param categoryId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<ProductListModel> selectByCategoryId(Integer categoryId) throws BusinessException {

    List<Product> productList = productMapper.selectByCategoryId(categoryId);
    if (productList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "无法找到识别该商品类别");
    }
    List<ProductListModel> productListModelList = ProductDOList2ProductModelListConver(productList);

    return productListModelList;
  }

  /**
   * 获取销量在前十的商品
   * @return
   * @throws BusinessException
   */
  @Override
  public List<ProductListModel> getTop10Products() throws BusinessException {
    List<Product> productList = productMapper.selectTop10Products();
    if (productList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "数据库无法调取前十销量产品");
    }
    productList=productList.subList(0, 10);
    List<ProductListModel> productListModelList = ProductDOList2ProductModelListConver(productList);
    return productListModelList;
  }

  /**
   * 通过商品id获取商品详情信息
   * @param productId
   * @return
   * @throws BusinessException
   */
  @Override
  public ProductDetailVO getProductById(Integer productId) throws BusinessException {
    Product product = productMapper.selectByPrimaryKey(productId);
    if (product == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "没有该商品");
    }
    ProductListModel productListModel = ProductDO2ProductModel(product);

    return ProductModelAndOrder2ProductDetailVOConver(productListModel);
  }

  /**
   * 为指定用户添加心愿单
   * @param userId
   * @param productId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean addToWishList(Integer userId, Integer productId) throws BusinessException {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.USER_NOT_EXIST);
    }
    Domitory domitory = domitoryMapper.selectByDomitoryNoAndBuildingId(user.getDomitoryNo(), user.getBuildingId());
    if (domitory == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户宿舍信息不存在");
    }
    //加入心愿单的商品必须该宿舍没有存量了。
    List<DomitoryStock> domitoryStockList = domitoryStockMapper.selectByDomitoryId(domitory.getId());
    for (DomitoryStock domitoryStock : domitoryStockList) {
      if (domitoryStock.getProductId().equals(productId)) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户宿舍该商品已存在，无需加入心愿单");
      }
    }
    WishList wishList = new WishList();
    wishList.setProductId(productId);
    wishList.setUserId(userId);
    wishList.setUserDomitoryid(domitory.getId());
    try {
      wishListMapper.insertSelective(wishList);
    } catch (DuplicateKeyException e) {
      throw new BusinessException(EnumBusinessError.UNKNOWN_ERROR, "该商品已在您的心愿单中了，请到心愿单查看");
    }
    return true;
  }

  /**
   * 获取指定用户所有心愿单的内容
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<WishListVO> getWishListByUserId(Integer userId) throws BusinessException {
    List<WishList> wishListList = wishListMapper.selectByUserId(userId);
    if (wishListList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    return wishListList2WishListVOListConvert(wishListList);
  }

  /**
   * 通过wishListId删除指定用户所选的心愿单
   * @param wishListId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean deleteWishListById(Integer wishListId) throws BusinessException {
    try {
      wishListMapper.deleteByPrimaryKey(wishListId);
    } catch (Exception e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "删除失败");
    }
    return true;
  }

  /**
   * 通过宿舍信息获取心愿单
   * @param domitoryId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<WishListVO> getWishListByDomitoryId(Integer domitoryId) throws BusinessException {
    if (domitoryId==null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<WishList> wishLists = wishListMapper.selectByDomitoryId(domitoryId);
    if (!(wishLists.size() > 0)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该宿舍没有心愿单");
    }
    List<WishListVO> wishListVOList = wishListList2WishListVOListConvert(wishLists);
    return wishListVOList;
  }

  /**
   * 扣除用户在宿舍中的库存
   * @param shoppingCartModelList
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void decreaseDomitoryStock(List<ShoppingCartModel> shoppingCartModelList,Integer domitoryId) throws BusinessException {
    for (ShoppingCartModel shoppingCartModel : shoppingCartModelList) {
      List<DomitoryStock> domitoryStockList = domitoryStockMapper.selectByProductId(shoppingCartModel.getProductId());
      if (domitoryStockList.size() == 0) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户宿舍无该商品");
      }
      for (DomitoryStock domitoryStock : domitoryStockList) {
        if (domitoryStock.getDomitoryId().equals(domitoryId)) {
          Integer result = domitoryStock.getDomitoryStock() - shoppingCartModel.getAmount();
          if (result == 0) {
            //删掉宿舍库存中该商品信息
            domitoryStockMapper.deleteByProductId(shoppingCartModel.getProductId());
          } else if (result < 0) {
            throw new BusinessException(EnumBusinessError.STOCK_NOT_ENOUGH, "宿舍库存不足");
          }
          //更新该商品在宿舍中的库存
          domitoryStock.setDomitoryStock(result);
          //进行扣库存
          domitoryStockMapper.updateByPrimaryKeySelective(domitoryStock);
        }
      }
    }
  }

  /**
   * 增加商品总表的商品销量
   * @param shoppingCartModelList
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void increaseSales(List<ShoppingCartModel> shoppingCartModelList) throws BusinessException {
    for (ShoppingCartModel shoppingCartModel : shoppingCartModelList) {
      Product product = productMapper.selectByPrimaryKey(shoppingCartModel.getProductId());
      if (product == null) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
      }
      //加销量
      int addSaleResult=product.getSales();
      addSaleResult += shoppingCartModel.getAmount();
      //扣库存
      int reduceStockResult = product.getStock();
      reduceStockResult-=shoppingCartModel.getAmount();
      //放进去然后更新
      product.setSales(addSaleResult);
      product.setStock(reduceStockResult);
      productMapper.updateByPrimaryKeySelective(product);
    }
  }

  /**
   * 根据用户编号获取所在宿舍的零食
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<DomitoryProductsVO2> getDomitoryProductsList(Integer userId) throws BusinessException {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"该用户不存在");
    }
    Domitory domitory = domitoryMapper.selectByDomitoryNoAndBuildingId(user.getDomitoryNo(), user.getBuildingId());
    List<DomitoryStock> domitoryStockList = domitoryStockMapper.selectByDomitoryId(domitory.getId());
    if (domitoryStockList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "数据库中，该宿舍无配零食");
    }
    List<DomitoryProductsVO> domitoryProductsVOList = domitoryStockList2DomitoryProductsVOListConvert(domitoryStockList);
    List<DomitoryProductsVO2> domitoryProductsVO2List = new ArrayList<>();
    for (DomitoryProductsVO domitoryProductsVO : domitoryProductsVOList) {
      DomitoryProductsVO2 domitoryProductsVO2 = new DomitoryProductsVO2();
      BeanUtils.copyProperties(domitoryProductsVO,domitoryProductsVO2);
      domitoryProductsVO2List.add(domitoryProductsVO2);
    }
    return domitoryProductsVO2List;
  }

  /**
   * 更新商品
   * @param product
   * @return
   * @throws BusinessException
   */
  @Override
  public boolean updateProduct(AdminUpdateProductForm product) throws BusinessException {
    Product product1 = new Product();
    BeanUtils.copyProperties(product,product1);
    try {
      productMapper.updateByPrimaryKeySelective(product1);
    } catch (Exception e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "更新商品失败");
    }
    return false;
  }



  //当初以为宿舍的首页界面，是通过客户的购物车下单，并计算才能实现。但其实是管理员自己加就好
  //当然，这个服务，可以用到后面的销售统计
//  @Override
//  public List<DomitoryProductsVO> getDomitoryProductsList(Integer userId) throws BusinessException {
//    //通过用户id查找他所在的宿舍
//    User user = userMapper.selectByPrimaryKey(userId);
//    if (user == null) {
//      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
//    }
//    List<TotalOrder> totalOrderList = totalOrderMapper.selectByDomitoryNo(user.getDomitoryNo());
//    List<OrderMaster> orderMasterList;
//    for (TotalOrder totalOrder : totalOrderList) {
//       orderMasterList = orderMasterMapper.selectByTotalorderSn(totalOrder.getOrderSn());
//    }
//    if (orderMasterList == null) {
//      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"该宿舍不曾下过订单");
//    }
//    Map<Integer, Integer> domitoryProducts = new HashMap<>();
//    for (OrderMaster orderMaster : orderMasterList) {
//      if (!domitoryProducts.containsKey(orderMaster.getProductId())) {
//        domitoryProducts.put(orderMaster.getProductId(), orderMaster.getAmount());
//      } else if (domitoryProducts.containsKey(orderMaster.getProductId())) {
//        int value = domitoryProducts.get(orderMaster.getProductId()).intValue();
//        value += orderMaster.getAmount();
//        domitoryProducts.put(orderMaster.getProductId(), value);
//      }
//    }
//    List<DomitoryProductsVO> domitoryProductsVOList = new ArrayList<>();

//  }

  //几个转换方法

  /**
   * ProductDO转换ProductModel
   * @param product
   * @return
   */
  @Override
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

  /**
   * ProductDOList转换ProductModelList
   * @param productList
   * @return
   */
  public List<ProductListModel> ProductDOList2ProductModelListConver(List<Product> productList) {
    return productList.stream().map(e ->
      ProductDO2ProductModel(e)
    ).collect(Collectors.toList());
  }

  /**
   * ProductModelAndOrder转换ProductDetailVO
   * @param productListModel
   * @return
   * @throws BusinessException
   */
  @Transactional
  public ProductDetailVO ProductModelAndOrder2ProductDetailVOConver(ProductListModel productListModel) throws BusinessException {

    List<OrderMaster> orderMasterList = orderMasterMapper.selectByProductId(productListModel.getProductId());

//    List<CommentModel> commentModelList = new ArrayList<>();
//    for (OrderMaster order : orderMasterList) {
//      CommentModel commentModel = new CommentModel();
//      commentModel.setProductId(order.getProductId());
//      commentModel.setComment(order.getComment());
//      String userName = userMapper.selectByPrimaryKey(order.getUserId()).getUserName();
//      if (userName == null) {
//        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该订单用户不存在");
//      }
//      commentModel.setUserName(userName);
//      commentModel.setCommentTime(order.getCommentTime());
//      commentModelList.add(commentModel);
//    }
    List<OrderCommentVO> orderCommentVOList = new ArrayList<>();
    for (OrderMaster orderMaster : orderMasterList) {
      if (orderMaster.getProductId().equals(productListModel.getProductId())) {
        List<OrderComment> orderComment = orderCommentMapper.selectByOrderMasterId(orderMaster.getId());
        if (orderComment.size() != 0) {
          OrderComment orderComment1=orderComment.get(0);
          OrderCommentVO orderCommentVO = new OrderCommentVO();
          BeanUtils.copyProperties(orderComment1,orderCommentVO);
          orderCommentVO.setImgNo1(orderComment1.getImg1());
          orderCommentVO.setImgNo2(orderComment1.getImg2());
          orderCommentVO.setImgNo3(orderComment1.getImg3());

          User user = userMapper.selectByPrimaryKey(orderMaster.getUserId());
          BeanUtils.copyProperties(user,orderCommentVO);
          SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          orderCommentVO.setCreateTime(format.format(orderMaster.getCommentTime()));
          orderCommentVOList.add(orderCommentVO);
        }
      }
    }
    ProductDetailVO productDetailVO = new ProductDetailVO();
    BeanUtils.copyProperties(productListModel,productDetailVO);

    productDetailVO.setOrderCommentVOList(orderCommentVOList);
    return productDetailVO;
  }

  /**
   * wishList转换Product
   * @param wishList
   * @return
   */
  private Product wishList2ProductConver(WishList wishList){
    Product product = productMapper.selectByPrimaryKey(wishList.getProductId());
    if (product == null) {
      return null;
    }
    return product;
  }

  /**
   * wishList转换Product
   * @param wishLists
   * @return
   */
  private List<Product> wishListList2ProductListConvert(List<WishList> wishLists) {
    return wishLists.stream().map(e ->
      wishList2ProductConver(e)
    ).collect(Collectors.toList());
  }

  private WishListVO wishList2WishListVOConvert(WishList wishList) {
    Product product = productMapper.selectByPrimaryKey(wishList.getProductId());
    if (product == null) {
      return null;
    }
    ProductListModel productListModel = ProductDO2ProductModel(product);
    WishListVO wishListVO = new WishListVO();
    BeanUtils.copyProperties(productListModel, wishListVO);
    wishListVO.setWishListId(wishList.getId());
    return wishListVO;
  }


  public List<WishListVO> wishListList2WishListVOListConvert(List<WishList> wishListList) {
    return wishListList.stream().map(e ->
      wishList2WishListVOConvert(e)
      ).collect(Collectors.toList());
  }

  /**
   * domitoryStock转换DomitoryProducts
   * @param domitoryStock
   * @return
   */
  private DomitoryProductsVO domitoryStock2DomitoryProductsVO(DomitoryStock domitoryStock) {
    DomitoryProductsVO domitoryProductsVO = new DomitoryProductsVO();
    BeanUtils.copyProperties(domitoryStock, domitoryProductsVO);
    Product product = productMapper.selectByPrimaryKey(domitoryProductsVO.getProductId());
    ProductListModel productListModel = ProductDO2ProductModel(product);
    BeanUtils.copyProperties(productListModel, domitoryProductsVO);
    return domitoryProductsVO;
  }

  /**
   * domitoryStockList转换DomitoryProductsVOList
   * @param domitoryStockList
   * @return
   */
  private List<DomitoryProductsVO> domitoryStockList2DomitoryProductsVOListConvert(List<DomitoryStock> domitoryStockList) {
    return domitoryStockList.stream().map(e ->
      domitoryStock2DomitoryProductsVO(e)
    ).collect(Collectors.toList());
  }
}
