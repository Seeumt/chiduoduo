package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.controller.vo.*;
import com.cumt.chiduoduo.convertor.Convertor;
import com.cumt.chiduoduo.dao.CategoryMapper;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.Category;
import com.cumt.chiduoduo.response.CommonReturnType;
import com.cumt.chiduoduo.service.ProductService;
import com.cumt.chiduoduo.service.model.ProductListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：商品管理api接口控制类
 *
 */
@RestController("product")
@RequestMapping("/product")
//defalut_allowed_headers允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
@CrossOrigin(allowCredentials = "true",allowedHeaders =  "*")
public class ProductController extends BaseController{

  @Autowired
  private ProductService productService;
  @Resource
  private CategoryMapper categoryMapper;


  Convertor convertor = new Convertor();

  /**
   * 获取商品类别列表
   * @return
   * @throws BusinessException
   */
  @RequestMapping("/getcategory")
  public CommonReturnType getCategory() throws BusinessException {
    List<Category> categoryList = productService.selectAllCategorys();
    return CommonReturnType.create(categoryList);
  }

  /**
   * 获取具体类别的商品列表
   * @param categoryId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getgoodsbycategory", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getGoodsByCategory(@RequestParam(name = "category_id") Integer categoryId) throws BusinessException {
    List<ProductListModel> productListModelList = productService.selectByCategoryId(categoryId);
    List<ProductListVO> productListVOList=convertor.productListModelList2ProductListVOListConvert(productListModelList);
    return CommonReturnType.create(productListVOList);
//    Category category = categoryMapper.selectById(categoryId);
//    return CommonReturnType.create(category);
  }

  /**
   * 选取销量前十的零食
   * @return
   * @throws BusinessException
   */
  @RequestMapping("/gettop10products")
  public CommonReturnType getTop10Products() throws BusinessException {
    List<ProductListModel> productListModelList = productService.getTop10Products();
    List<ProductListVO> productListVOList=convertor.productListModelList2ProductListVOListConvert(productListModelList);
    return CommonReturnType.create(productListVOList);
  }

  /**
   * 根据商品id获得商品详情页面
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getproductdetail", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getProductDetail(@RequestParam(name = "product_id") Integer productId) throws BusinessException {
    if (productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    ProductDetailVO productDetailVO=productService.getProductById(productId);
    return CommonReturnType.create(productDetailVO);
  }

  /**
   * 将喜欢的商品加入心愿单
   * @param productId
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/addtowishlist", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType addToWishList(@RequestParam(name = "product_id") Integer productId,
                                        @RequestParam(name = "user_id") Integer userId) throws BusinessException {
    if (productId == null || userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    productService.addToWishList(userId, productId);
    return CommonReturnType.create(null);
  }

  /**
   * 获取指定用户的心愿单列表
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getwishlist", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getWishList(@RequestParam(name = "user_id") Integer userId) throws BusinessException {
    if (userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<WishListVO> wishListVOList= productService.getWishListByUserId(userId);
    return CommonReturnType.create(wishListVOList);
  }

  /**
   * 删除指定用户的心愿单
   * @param wishListId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deletewishlist", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteWishList(@RequestParam(name = "wishListId") Integer wishListId) throws BusinessException {
    productService.deleteWishListById(wishListId);
    return CommonReturnType.create(null);
  }

  /**
   * 根据用户编号获取所在宿舍的零食
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getdomitorygoodsbyuserid", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getDomitoryGoodsByuserId(@RequestParam(name = "user_id") Integer userId) throws BusinessException {
    if (userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
  //获取宿舍内零食列表
    List<DomitoryProductsVO2> domitoryProductsVO2List = productService.getDomitoryProductsList(userId);
    return CommonReturnType.create(domitoryProductsVO2List);
  }
}
