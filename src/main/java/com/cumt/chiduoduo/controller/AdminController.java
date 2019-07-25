package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.controller.BaseController;
import com.cumt.chiduoduo.controller.Form.AdminAddProductForm;
import com.cumt.chiduoduo.controller.Form.AdminUpdateProductForm;
import com.cumt.chiduoduo.controller.Form.UserUpdateForm;
import com.cumt.chiduoduo.controller.vo.*;
import com.cumt.chiduoduo.dao.DomitoryMapper;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.response.CommonReturnType;
import com.cumt.chiduoduo.service.*;
import com.cumt.chiduoduo.service.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：管理员api接口控制类
 *
 */
@Slf4j
@RestController("admin")
@RequestMapping("/admin")
//defalut_allowed_headers允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class AdminController extends BaseController {

  @Autowired
  private AdminService adminService;
  @Autowired
  private ProductService productService;
  @Autowired
  private UserService userService;


  /**
   * //获取数据库商品总表中所有商品
   *
   * @return
   * @throws BusinessException
   */
  @RequestMapping("/getallproducts")
  public CommonReturnType getAllProducts() throws BusinessException {
    List<Product> productList = adminService.getAllProduct();
    return CommonReturnType.create(productList);
  }

  /**
   * //删除商品总表某商品
   *
   * @param productId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deleteproductbyid", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteProductById(@RequestParam(name = "product_id") Integer productId) throws BusinessException {
    adminService.deleteProductByProductId(productId);
    return CommonReturnType.create(null);
  }


  /**
   * //给商品总表添加新产品
   *
   * @param adminAddProductForm
   * @param bindingResult
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/addproduct", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType addProduct(@Valid AdminAddProductForm adminAddProductForm,
                                     BindingResult bindingResult) throws BusinessException {
    if (bindingResult.hasErrors()) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "管理员添加商品参数错误");
    }
    adminService.addNewProduct(adminAddProductForm);
    return CommonReturnType.create(null);
  }


  /**
   * //管理员获取指定宿舍心愿单
   * //TODO 接受buildingId跟domitoryNo的打包数据
   *
   * @param domitoryId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getwishlistbydomitorynumber", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getWishListByDomitoryNumber(@RequestParam(name = "domitory_id") Integer domitoryId) throws BusinessException {
    List<WishListVO> wishListVOList = adminService.getWishListByDomitoryId(domitoryId);
    //TODO 此处返给前端的心愿单要有用户信息
    return CommonReturnType.create(wishListVOList);
  }

//TODO 接受管理员给宿舍配的零食信息后，入库domitory_stock_management表，注意与购物车类似的高要求。

  //TODO 管理员给宿舍配零食后，总商品表的stock要扣除，宿舍内对添加的每个商品加到domitory_stock表中。
  //TODO 同时完成心愿单的配送后，要清除该宿舍的所有心愿单
  //TODO userController页的根据buildingId寻找宿舍号功能


  /**
   * //管理员根据userId对用户进行删除
   *
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deletebyuserid", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteByUserId(@RequestParam(name = "user_id") Integer userId) throws BusinessException {
    if (userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    adminService.deleteByUserId(userId);
    return CommonReturnType.create(null);
  }


  /**
   * //管理员对用户进行修改
   *
   * @param userUpdateForm
   * @param bindingResult
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/updateuser", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType updateUser(@Valid UserUpdateForm userUpdateForm,
                                     BindingResult bindingResult) throws BusinessException {
    if (bindingResult.hasErrors()) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "传入的用户信息有误");
    }
    User user = new User();
    BeanUtils.copyProperties(userUpdateForm, user);
    user.setId(userUpdateForm.getUserId());
    adminService.updateUser(user);
    return CommonReturnType.create(user);
  }


  /**
   * //管理员根据userId查询某个用户信息（由于密码表单独设，管理员无法查询到或者修改到用户密码）
   *
   * @param userId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getuser", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getUser(@RequestParam("user_id") Integer userId) throws BusinessException {
    if (userId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    User user = adminService.getUser(userId);
    return CommonReturnType.create(user);
  }

  /**
   * //管理员获取所有用户信息列表
   *
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getallusers")
  public CommonReturnType getAllUsers() throws BusinessException {
    List<UserModelVO> userModelVOList = adminService.getAllUsers();
    return CommonReturnType.create(userModelVOList);
  }


  /**
   * //管理员根据具体商品id查看销售统计
   *
   * @param categoryId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/salestatistics", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType saleStatistics(@RequestParam("category_id") Integer categoryId) throws BusinessException {
    if (categoryId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<SaleStatisticsVO> saleStatisticsVOList = adminService.getProductSaleStatistics(categoryId);
    return CommonReturnType.create(saleStatisticsVOList);
  }


  /**
   * //管理员查看树形商品结构
   *
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/showtreeproduct")
  public CommonReturnType showTreeProduct() throws BusinessException {
    List<CategoryTreeVo> categoryTreeVoList = adminService.getCategoryTreeVoList();
    return CommonReturnType.create(categoryTreeVoList);
  }


  /**
   * //管理员注册用户注册接口
   *
   * @param name
   * @param buildingId
   * @param domitoryNo
   * @param schoolId
   * @param email
   * @param telphone
   * @param password
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/adminregisteruser", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType register(@RequestParam(name = "name") String name,
                                   @RequestParam(name = "building_id") Integer buildingId,
                                   @RequestParam(name = "domitory_No") String domitoryNo,
                                   @RequestParam(name = "school_id") Integer schoolId,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "telphone") String telphone,
                                   @RequestParam(name = "password") String password
  ) throws BusinessException {
    UserModel userModel = new UserModel();
    userModel.setUserName(name);
    userModel.setSchoolId(schoolId);
    userModel.setBuildingId(buildingId);
    userModel.setEmail(email);
    userModel.setDomitoryNo(domitoryNo);
    userModel.setTelphone(telphone);
    userModel.setLastResetPasswordTime(new Date());
    //对密码进行加密处理

    String BCryptPassword = new BCryptPasswordEncoder().encode(password);
    userModel.setPassword(BCryptPassword);
//将userModel的对象完善后，调用service层的注册方法进行用户注册
    userService.register(userModel);
    return CommonReturnType.create(null);
  }


  /**
   * //管理员修改商品
   *
   * @param product
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/updateproduct", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteByUserId(@Valid AdminUpdateProductForm product) throws BusinessException {

    if (product == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    productService.updateProduct(product);
    return CommonReturnType.create(null);
  }


  /**
   * //管理员查看在线用户
   *
   * @return
   */
  @RequestMapping(value = "/getallonlineusers")
  public CommonReturnType getAllOnlineUsers() {
    List<OnlineUserVO> onlineUserVOList = adminService.getAllOnlineUsers();
    return CommonReturnType.create(onlineUserVOList);
  }


  /**
   * //通过宿舍楼id查询该楼所有宿舍号情况
   *
   * @param buildingId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getdomitorybybuildingid", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getDomitoryByBuildingId(@RequestParam(name = "building_id") Integer buildingId) throws BusinessException {
    if (buildingId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<Domitory> domitoryList = adminService.getAllDomitories(buildingId);
    return CommonReturnType.create(domitoryList);
  }


  /**
   * //获取树形商品结构
   *
   * @return
   */
  @RequestMapping(value = "/getproducttree")
  public CommonReturnType getProductTree() {
    List<TreeVO> treeVOList = adminService.getProductTree();
    return CommonReturnType.create(treeVOList);
  }


  /**
   * //给指定宿舍配送定量零食deliveryproducts
   *
   * @param domitoryId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deliveryproducts", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deliveryProducts(@RequestParam(name = "domitory_id") Integer domitoryId) throws BusinessException {
    if (domitoryId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    boolean result = adminService.deliveryProducts(domitoryId);
    return CommonReturnType.create(result);
  }

  /**
   * 删除最高级类别
   *
   * @param highestCategoryId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/deletehighestcategory", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType deleteHighestCategory(@RequestParam(name = "id") Integer highestCategoryId) throws BusinessException {
    if (highestCategoryId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    adminService.deleteHighestCategory(highestCategoryId);
    return CommonReturnType.create(null);
  }

  /**
   * 增加最高类目
   *
   * @param highestCategoryName
   * @return
   */
  @RequestMapping(value = "/addhighestcategory", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType addHighestCategory(@RequestParam(name = "name") String highestCategoryName) throws BusinessException {
    if (highestCategoryName == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    adminService.addHighestCategory(highestCategoryName);
    return CommonReturnType.create(null);
  }

  /**
   * 修改最高级类别
   *
   * @param highestCategoryId
   * @param highestCategoryName
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/updatehighestcategory", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType updateHighestCategory(@RequestParam(name = "highestCategoryId") Integer highestCategoryId,
                                                @RequestParam(name = "highestCategoryName") String highestCategoryName) throws BusinessException {
    if (highestCategoryName == null || highestCategoryId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    adminService.updateHighestCategory(highestCategoryId, highestCategoryName);
    return CommonReturnType.create(null);
  }

    /**
     * 删除二级级类别
     *
     * @param categoryId
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/deletecategory", method = RequestMethod.POST,
            consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType deleteCategory (@RequestParam(name = "categoryId") Integer categoryId) throws
    BusinessException {
      if (categoryId == null) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
      }
      adminService.deleteCategory(categoryId);
      return CommonReturnType.create(null);
    }

    /**
     * 增加二级类别
     *
     * @param categoryName
     * @param highestCategoryId
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/addcategory", method = RequestMethod.POST,
            consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType addCategory (@RequestParam(name = "categoryName") String categoryName,
            @RequestParam(name = "highestCategoryId") Integer highestCategoryId) throws BusinessException {
      if (categoryName == null || highestCategoryId == null) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
      }
      adminService.addCategory(categoryName, highestCategoryId);
      return CommonReturnType.create(null);
    }

    /**
     * 修改二级商品类别
     *
     * @param categoryName
     * @param highestCategoryId
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/updatecategory", method = RequestMethod.POST,
            consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType updateCategory (@RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "categoryName") String categoryName,
            @RequestParam(name = "highestCategoryId") Integer highestCategoryId) throws BusinessException {
      if (categoryName == null || highestCategoryId == null || categoryId == null) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
      }
      adminService.updateCategory(categoryId, categoryName, highestCategoryId);
      return CommonReturnType.create(null);
    }
  }
