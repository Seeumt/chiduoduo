package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.controller.Form.AdminAddProductForm;
import com.cumt.chiduoduo.controller.vo.*;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Domitory;
import com.cumt.chiduoduo.model.Product;
import com.cumt.chiduoduo.model.User;
import com.cumt.chiduoduo.model.UserOnlineStatistics;
import com.cumt.chiduoduo.service.model.UserModel;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：管理员服务接口
 *
 */
public interface AdminService {
  //对总商品表进行增删改查

  void deleteProductByProductId(Integer productId) throws BusinessException;

  void updateProduct(Product product);

  List<Product> getAllProduct() throws BusinessException;

  //管理员给商品总表添加新产品
  boolean addNewProduct(AdminAddProductForm adminAddProductForm) throws BusinessException;

  List<WishListVO> getWishListByDomitoryId(Integer domitoryId) throws BusinessException;
  //管理员根据userId对用户进行删除
  boolean deleteByUserId(Integer userId) throws BusinessException;

  //管理员对用户进行修改
  boolean updateUser(User user) throws BusinessException;
  //管理员根据userId查询某个用户信息（由于密码表单独设，管理员无法查询到或者修改到用户密码）
  User getUser(Integer userId) throws BusinessException;

  //管理员根据具体商品id查看销售统计
  List<SaleStatisticsVO> getProductSaleStatistics(Integer categoryId) throws BusinessException;

  //管理员获取所有用户信息列表
  List<UserModelVO> getAllUsers() throws BusinessException;

  List<CategoryTreeVo> getCategoryTreeVoList();

  //获取在线用户
  List<OnlineUserVO> getAllOnlineUsers();

  //获取指定宿舍楼里的宿舍号信息
  List<Domitory> getAllDomitories(Integer buildingId) throws BusinessException;

  //获取树形商品结构
  List<TreeVO> getProductTree();

  boolean deliveryProducts(Integer domitoryId) throws BusinessException;

  /**
   * 删除最高级类目
   * @param highestCategoryId
   * @return
   */
  boolean deleteHighestCategory(Integer highestCategoryId);

  /**
   * 增加最高类目
   * @param highestCategoryName
   * @return
   */
  boolean addHighestCategory(String highestCategoryName);

  boolean deleteCategory(Integer categoryId);

  /**
   * 增加二级类别
   * @param categoryName
   * @param highestCategoryId
   * @return
   */
  boolean addCategory(String categoryName, Integer highestCategoryId);

  /**
   * 修改二级商品类别
   * @param categoryName
   * @param highestCategoryId
   * @return
   */
  boolean updateCategory(Integer categoryId,String categoryName, Integer highestCategoryId) throws BusinessException;

  /**
   * 修改最高级类别
   * @param highestCategoryId
   * @param highestCategoryName
   * @return
   */
  boolean updateHighestCategory(Integer highestCategoryId, String highestCategoryName) throws BusinessException;
}
