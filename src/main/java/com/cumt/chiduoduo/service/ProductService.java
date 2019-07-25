package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.controller.Form.AdminUpdateProductForm;
import com.cumt.chiduoduo.controller.vo.DomitoryProductsVO2;
import com.cumt.chiduoduo.controller.vo.ProductDetailVO;
import com.cumt.chiduoduo.controller.vo.WishListVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Category;
import com.cumt.chiduoduo.model.Product;
import com.cumt.chiduoduo.service.model.ProductListModel;
import com.cumt.chiduoduo.service.model.ShoppingCartModel;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：产品服务接口
 *
 */
public interface ProductService {

  List<Category> selectAllCategorys() throws BusinessException;

  List<ProductListModel> selectByCategoryId(Integer categoryId) throws BusinessException;

  List<ProductListModel> getTop10Products() throws BusinessException;

  ProductDetailVO getProductById(Integer productId) throws BusinessException;

  boolean addToWishList(Integer userId, Integer productId) throws BusinessException;

  List<WishListVO> getWishListByUserId(Integer userId) throws BusinessException;

  boolean deleteWishListById(Integer wishListId) throws BusinessException;
  List<WishListVO> getWishListByDomitoryId(Integer domitoryId) throws BusinessException;
  void decreaseDomitoryStock(List<ShoppingCartModel> shoppingCartModelList,Integer domitoryId) throws BusinessException;

  //TODO 因为管理员配零食的功能没做，所以直接在这里扣商品总表库存
  //增加商品总表的商品销量
  void increaseSales(List<ShoppingCartModel> shoppingCartModelList) throws BusinessException;


  //管理员加商品到各个宿舍后，商品总表的库存减少
//TODO  void decreaseTotalStock()

  List<DomitoryProductsVO2> getDomitoryProductsList(Integer userId) throws BusinessException;

  ProductListModel ProductDO2ProductModel(Product product);

  boolean updateProduct(AdminUpdateProductForm adminUpdateProductForm) throws BusinessException;

}
