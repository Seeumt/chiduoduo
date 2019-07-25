package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.vo.DomitoryProductsVO2;
import com.cumt.chiduoduo.controller.vo.ProductDetailVO;
import com.cumt.chiduoduo.controller.vo.WishListVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Category;
import com.cumt.chiduoduo.service.model.ProductListModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：产品服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void selectAllCategorys() throws BusinessException {
        List<Category> categoryList = productService.selectAllCategorys();
        Assert.assertNotEquals(0,categoryList.size());
    }

    @Test
    public void selectByCategoryId() throws BusinessException {
        List<ProductListModel> productListModelList = productService.selectByCategoryId(1);
        Assert.assertNotEquals(0,productListModelList.size());

    }

    @Test
    public void getTop10Products() throws BusinessException {
        List<ProductListModel> productListModelList = productService.getTop10Products();
        Assert.assertNotEquals(0,productListModelList.size());
    }

    @Test
    public void getProductById() throws BusinessException {
        ProductDetailVO productDetailVO = productService.getProductById(12);
        Assert.assertEquals(12,(long)productDetailVO.getProductId());

    }

    @Test
    public void addToWishList() throws BusinessException {
        productService.addToWishList(20950,13);
    }

    @Test
    public void getWishListByUserId() throws BusinessException {
        List<WishListVO> wishListVOList = productService.getWishListByUserId(3);
        Assert.assertNotEquals(0,wishListVOList.size());
    }

    @Test
    public void getWishListByDomitoryId() throws BusinessException {
        List<WishListVO> wishListVOList = productService.getWishListByDomitoryId(1);
        Assert.assertNotEquals(0,wishListVOList.size());
    }

    @Test
    public void getDomitoryProductsList() throws BusinessException {
        List<DomitoryProductsVO2> domitoryProductsVO2List = productService.getDomitoryProductsList(20950);
        Assert.assertNotEquals(0,domitoryProductsVO2List.size());
    }
}