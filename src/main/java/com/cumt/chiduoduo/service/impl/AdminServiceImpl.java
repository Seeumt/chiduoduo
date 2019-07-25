package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.Form.AdminAddProductForm;
import com.cumt.chiduoduo.controller.vo.*;
import com.cumt.chiduoduo.convertor.Convertor;
import com.cumt.chiduoduo.dao.*;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.response.CommonReturnType;
import com.cumt.chiduoduo.service.AdminService;
import com.cumt.chiduoduo.service.BuildingService;
import com.cumt.chiduoduo.service.ProductService;
import com.cumt.chiduoduo.service.SchoolService;
import com.cumt.chiduoduo.service.model.ProductListModel;
import com.cumt.chiduoduo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：管理员服务层实现类
 *
 */
@Service
public class AdminServiceImpl implements AdminService {
  @Resource
  private ProductMapper productMapper;
  @Autowired
  private ProductService productService;
  @Resource
  private DomitoryMapper domitoryMapper;
  @Resource
  private UserMapper userMapper;
  @Resource
  private CategoryMapper categoryMapper;
  @Resource
  private SchoolMapper schoolMapper;
  @Resource
  private BuildingMapper buildingMapper;
  @Resource
  private UserPasswordMapper userPasswordMapper;
  @Resource
  private UserOnlineStatisticsMapper userOnlineStatisticsMapper;
  @Resource
  private HighestCategoryMapper highestCategoryMapper;
  @Resource
  private DomitoryStockMapper domitoryStockMapper;


  @Override
  public List<Product> getAllProduct() throws BusinessException {
    List<Product> productList = productMapper.selectAllProductsToAdmin();
    if (!(productList.size() > 0)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    return productList;
  }


  /**
   * 删除商品总表的特定商品
   * @param productId
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void deleteProductByProductId(Integer productId) throws BusinessException {
    if (productId == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    productMapper.deleteByPrimaryKey(productId);
  }

  /**
   * 修改商品总表的特定商品
   * @param product
   */
  @Override
  public void updateProduct(Product product) {
    productMapper.updateByPrimaryKeySelective(product);
  }

  /**
   * 管理员给商品总表添加新产品
   * @param adminAddProductForm
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean addNewProduct(AdminAddProductForm adminAddProductForm) throws BusinessException {
    Product product = new Product();
    BeanUtils.copyProperties(adminAddProductForm, product);
    try {
      productMapper.insertSelective(product);
    } catch (Exception e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "添加新商品失败");
    }
    return true;
  }

  @Override
  public List<WishListVO> getWishListByDomitoryId(Integer domitoryId) throws BusinessException {
    return productService.getWishListByDomitoryId(domitoryId);
  }

  /**
   * 管理员根据userId对用户进行删除
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean deleteByUserId(Integer userId) throws BusinessException {
    try {
      userMapper.deleteByPrimaryKey(userId);
    } catch (Exception e) {
      throw new BusinessException(EnumBusinessError.USER_NOT_EXIST);
    }
    return true;
  }

  /**
   * 管理员对用户进行修改
   * @param user
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean updateUser(User user) throws BusinessException {
    try {
      userMapper.updateByPrimaryKeySelective(user);
    } catch (DuplicateKeyException e) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "与数据库原有的其他数据中手机或邮箱重复");
    }
    return true;
  }

  /**
   * 管理员根据userId查询某个用户信息（由于密码表单独设，管理员无法查询到或者修改到用户密码）
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  public User getUser(Integer userId) throws BusinessException {
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户在数据库中不存在");
    }
    return user;
  }

  /**
   * 管理员获取销售数据
   * @param categoryId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<SaleStatisticsVO> getProductSaleStatistics(Integer categoryId) throws BusinessException {
    List<Product> productList = productMapper.selectByCategoryId(categoryId);
    if (productList == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<ProductListModel> productListModelList = ProductDOList2ProductModelListConver(productList);
    List<SaleStatisticsVO> saleStatisticsVOList = productModelList2SaleStatisticsVOListConvert(productListModelList);
    return saleStatisticsVOList;
  }

  /**
   * 管理员获取所有用户信息列表
   * @return
   * @throws BusinessException
   */
  @Override
  public List<UserModelVO> getAllUsers() throws BusinessException {
    List<User> userList = userMapper.selectAllUsers();
    if (userList.isEmpty()) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<UserModel> userModelList = userList2UserModelListConvert(userList);
    if (userModelList.isEmpty()) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<UserModelVO> userModelVOList = userModelList2UserModelVOListConvert(userModelList);
    return userModelVOList;
  }

  /**
   * 获取二级商品类别树
   * @return
   */
  @Override
  public List<CategoryTreeVo> getCategoryTreeVoList() {
    List<CategoryTreeVo> categoryTreeVoList = new ArrayList<>();
    List<Category> categoryList = categoryMapper.selectAllCategorys();
    categoryTreeVoList = categoryList2CategoryTreeVoListConvert(categoryList);
    return categoryTreeVoList;
  }

  /**
   * 获取在线用户
   * @return
   */
  @Override
  public List<OnlineUserVO> getAllOnlineUsers() {
    List<UserOnlineStatistics> userOnlineStatisticsList = userOnlineStatisticsMapper.selectOnlineUser("在线");
    List<OnlineUserVO> onlineUserVOList = new ArrayList<>();
    for (UserOnlineStatistics userOnlineStatistics : userOnlineStatisticsList) {
      OnlineUserVO onlineUserVO = new OnlineUserVO();
      BeanUtils.copyProperties(userOnlineStatistics,onlineUserVO);
      SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
      String time = format.format(userOnlineStatistics.getLoginTime().getTime());
      onlineUserVO.setLoginTime(time);
      onlineUserVOList.add(onlineUserVO);
    }
    return onlineUserVOList;
  }

  /**
   * 获取指定宿舍楼里的宿舍号信息
   * @param buildingId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<Domitory> getAllDomitories(Integer buildingId) throws BusinessException {
    Building building = buildingMapper.selectByPrimaryKey(buildingId);
    if (building == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "不存在该宿舍楼");
    }
    List<Domitory> domitoryList = domitoryMapper.selectByBuildingId(buildingId);

    return domitoryList;
  }

  /**
   * 获取树形商品结构
   * @return
   */
  @Override
  public List<TreeVO> getProductTree() {
    List<TreeVO> treeVOList = new ArrayList<>();
    List<HighestCategory> highestCategoryList = highestCategoryMapper.selectAllHighestCategory();
    List<CategoryTreeVo> categoryTreeVoList = getCategoryTreeVoList();

    for (HighestCategory highestCategory : highestCategoryList) {
      List<CategoryTreeVo> categoryTreeVoList1 = new ArrayList<>();
      for (CategoryTreeVo categoryTreeVo : categoryTreeVoList) {
        if (highestCategory.getId().equals(categoryTreeVo.getHighestCategoryId())) {
          categoryTreeVoList1.add(categoryTreeVo);
        }
      }
      TreeVO treeVO = new TreeVO();
      BeanUtils.copyProperties(highestCategory, treeVO);
      treeVO.setCategoryTreeVoList(categoryTreeVoList1);
      treeVOList.add(treeVO);
    }
    return treeVOList;
  }

  /**
   * 通过宿舍id配送商品
   * @param domitoryId
   * @return
   * @throws BusinessException
   */
  @Override
  @Transactional
  public boolean deliveryProducts(Integer domitoryId) throws BusinessException {
    Domitory domitory = domitoryMapper.selectByPrimaryKey(domitoryId);
    if (domitory == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    for (int i = 1; i < 6; i++) {
      DomitoryStock domitoryStock = new DomitoryStock();
      domitoryStock.setDomitoryId(domitoryId);
      domitoryStock.setProductId(i);
      Product product = productMapper.selectByPrimaryKey(i);
      domitoryStock.setProductName(product.getName());
      domitoryStock.setDomitoryStock(20);
      domitoryStock.setCategoryId(product.getCategoryId());
      Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
      domitoryStock.setCategoryName(category.getName());
      domitoryStockMapper.insertSelective(domitoryStock);
    }
    return true;
  }

  /**
   * 删除最高级类目
   * @param highestCategoryId
   * @return
   */
  @Override
  @Transactional
  public boolean deleteHighestCategory(Integer highestCategoryId) {
    HighestCategory highestCategory = highestCategoryMapper.selectByPrimaryKey(highestCategoryId);
    if (highestCategory == null) {
      return false;
    }
    highestCategoryMapper.deleteByPrimaryKey(highestCategoryId);
    return true;
  }

  /**
   * 增加最高类目
   * @param highestCategoryName
   * @return
   */
  @Override
  @Transactional
  public boolean addHighestCategory(String highestCategoryName) {
    HighestCategory highestCategory = new HighestCategory();
    highestCategory.setName(highestCategoryName);
    highestCategoryMapper.insertSelective(highestCategory);
    return true;
  }

  /**
   * 删除二级类目
   * @param categoryId
   * @return
   */
  @Override
  @Transactional
  public boolean deleteCategory(Integer categoryId) {
    Category category = categoryMapper.selectByPrimaryKey(categoryId);
    if (category == null) {
      return false;
    }
    categoryMapper.deleteByPrimaryKey(categoryId);
    return true;
  }

  /**
   * 增加二级类别
   * @param categoryName
   * @param highestCategoryId
   * @return
   */
  @Override
  @Transactional
  public boolean addCategory(String categoryName, Integer highestCategoryId) {
    Category category = new Category();
    category.setName(categoryName);
    category.setHighestCategoryId(highestCategoryId);
    categoryMapper.insertSelective(category);
    return true;
  }

  /**
   * 修改二级商品类别
   * @param categoryName
   * @param highestCategoryId
   * @return
   */
  @Override
  @Transactional
  public boolean updateCategory(Integer categoryId,String categoryName, Integer highestCategoryId) throws BusinessException {
    Category category = categoryMapper.selectByPrimaryKey(categoryId);
    if (category == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    Category category1 = new Category();
    BeanUtils.copyProperties(category, category1);
    category1.setName(categoryName);
    category1.setHighestCategoryId(highestCategoryId);
    categoryMapper.updateByPrimaryKeySelective(category1);
    return true;
  }

  /**
   *修改最高级类别
   * @param highestCategoryId
   * @param highestCategoryName
   * @return
   */
  @Override
  @Transactional
  public boolean updateHighestCategory(Integer highestCategoryId, String highestCategoryName) throws BusinessException {
    HighestCategory highestCategory = highestCategoryMapper.selectByPrimaryKey(highestCategoryId);
    if (highestCategory == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    HighestCategory highestCategory1 = new HighestCategory();
    BeanUtils.copyProperties(highestCategory, highestCategory1);
    highestCategory1.setName(highestCategoryName);
    highestCategoryMapper.updateByPrimaryKeySelective(highestCategory1);
    return true;
  }

  /**
   * ProductDO转换ProductModel
   * @param product
   * @return
   */
  private ProductListModel ProductDO2ProductModel(Product product) {
    ProductListModel productListModel = new ProductListModel();
    BeanUtils.copyProperties(product, productListModel);
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
   * 把ProductDOList转换成ProductModelList
   * @param productList
   * @return
   */
  public List<ProductListModel> ProductDOList2ProductModelListConver(List<Product> productList) {
    return productList.stream().map(e ->
            ProductDO2ProductModel(e)
    ).collect(Collectors.toList());
  }

  /**
   * productModel转换为SaleStatisticsVO
   * @param productListModel
   * @return
   */
  private SaleStatisticsVO productModel2SaleStatisticsVOConvert(ProductListModel productListModel) {
    SaleStatisticsVO saleStatisticsVO = new SaleStatisticsVO();
    BeanUtils.copyProperties(productListModel, saleStatisticsVO);
    return saleStatisticsVO;
  }

  /**
   * productModelList转换为SaleStatisticsVOList
   * @param productListModelList
   * @return
   */
  private List<SaleStatisticsVO> productModelList2SaleStatisticsVOListConvert(List<ProductListModel> productListModelList) {
    return productListModelList.stream().map(e ->
            productModel2SaleStatisticsVOConvert(e)
    ).collect(Collectors.toList());
  }

  /**
   * user转换为userModel
   * @param user
   * @return
   */
  private UserModel user2userModelConvert(User user) {
    UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
    School school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
    Building building = buildingMapper.selectByPrimaryKey(user.getBuildingId());
    Convertor convertor = new Convertor();
    UserModel userModel = convertor.dataObject2UserModel(user, userPassword, building, school);
    return userModel;
  }

  /**UserModelList
   * userList转换为
   * @param userList
   * @return
   */
  private List<UserModel> userList2UserModelListConvert(List<User> userList) {
    return userList.stream().map(e ->
            user2userModelConvert(e)
    ).collect(Collectors.toList());
  }

  /**
   * userModel转换为UserModelVo
   * @param userModel
   * @return
   */
  private UserModelVO userModel2UserModelVoConvert(UserModel userModel) {
    UserModelVO userModelVO = new UserModelVO();
    BeanUtils.copyProperties(userModel, userModelVO);
    return userModelVO;
  }

  /**
   * userModelList转换为UserModelVOList
   * @param userModelList
   * @return
   */
  private List<UserModelVO> userModelList2UserModelVOListConvert(List<UserModel> userModelList) {
    return userModelList.stream().map(e ->
            userModel2UserModelVoConvert(e)
    ).collect(Collectors.toList());
  }

  /**
   * category转换为CategoryTreeVo
   * @param category
   * @return
   */
  private CategoryTreeVo category2CategoryTreeVoConvert(Category category) {
    CategoryTreeVo categoryTreeVo = new CategoryTreeVo();
    BeanUtils.copyProperties(category, categoryTreeVo);
    List<Product> productList = productMapper.selectByCategoryId(category.getId());
    List<ProductListModel> productListModelList = ProductDOList2ProductModelListConver(productList);
    Convertor convertor = new Convertor();
    List<ProductListVO> productListVOList = convertor.productListModelList2ProductListVOListConvert(productListModelList);
    categoryTreeVo.setProductListVOList(productListVOList);
    return categoryTreeVo;
  }

  /**
   * categoryList转换为CategoryTreeVoList
   * @param categoryList
   * @return
   */
  private List<CategoryTreeVo> categoryList2CategoryTreeVoListConvert(List<Category> categoryList) {
   return categoryList.stream().map(e ->
            category2CategoryTreeVoConvert(e)
    ).collect(Collectors.toList());
  }

  /*上面是几个转换方法*/
}
