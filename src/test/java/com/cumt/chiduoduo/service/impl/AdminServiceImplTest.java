package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.Form.AdminAddProductForm;
import com.cumt.chiduoduo.controller.vo.CategoryTreeVo;
import com.cumt.chiduoduo.controller.vo.SaleStatisticsVO;
import com.cumt.chiduoduo.controller.vo.UserModelVO;
import com.cumt.chiduoduo.controller.vo.WishListVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Product;
import com.cumt.chiduoduo.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：管理员服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceImplTest {
    @Autowired
    private AdminServiceImpl adminService;

    @Test
    public void getAllProduct() throws BusinessException {
        List<Product> productList = adminService.getAllProduct();
        Assert.assertNotEquals(0,productList.size());
    }

    @Test
    public void updateProduct() {
        Product product = new Product();
        product.setId(22);
        product.setCategoryId(5);
        product.setDescription("100g/袋，千年传承，肉中精华。传统美食那么多，但没有人不爱猪肉铺。食用方便，制作考究，美味可口的中式传统美食。");
        product.setName("咸猪肉铺");
        product.setPrice(new BigDecimal(5));
        product.setStock(100);
        product.setSales(50);
        product.setImgNo1("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/35.jpg");
        product.setImgNo2("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/36.jpg");
        product.setImgNo3("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/37.jpg");
        adminService.updateProduct(product);
    }

    @Test
    public void addNewProduct() throws BusinessException {
        AdminAddProductForm adminAddProductForm = new AdminAddProductForm();
        adminAddProductForm.setCategoryId(5);
        adminAddProductForm.setDescription("真好吃");
        adminAddProductForm.setName("咸猪肉铺");
        adminAddProductForm.setPrice(new BigDecimal(5));
        adminAddProductForm.setStock(100);
        adminAddProductForm.setImgNo1("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/35.jpg");
        adminAddProductForm.setImgNo2("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/36.jpg");
        adminAddProductForm.setImgNo3("https://wishlist-1257728329.cos.ap-beijing.myqcloud.com/37.jpg");
        boolean result = adminService.addNewProduct(adminAddProductForm);
        Assert.assertTrue(result);
    }

    @Test
    public void getWishListByDomitoryId() throws BusinessException {
        List<WishListVO> wishListVOList = adminService.getWishListByDomitoryId(1);
        Assert.assertNotEquals(0,wishListVOList.size());
    }

    @Test
    public void updateUser() throws BusinessException {
        User user = new User();
        user.setId(20950);
        user.setBuildingId(1);
        user.setDomitoryNo("A4012");
        user.setSchoolId(1);
        user.setTelphone("157875080");
        user.setEmail("5514@qq.com");
        user.setUserName("zhihang");
        adminService.updateUser(user);
    }

    @Test
    public void getProductSaleStatistics() throws BusinessException {
        List<SaleStatisticsVO> saleStatisticsVOList = adminService.getProductSaleStatistics(1);
        Assert.assertNotEquals(0,saleStatisticsVOList.size());
    }

    @Test
    public void getAllUsers() throws BusinessException {
        List<UserModelVO> userModelVOList = adminService.getAllUsers();
        Assert.assertNotEquals(0,userModelVOList.size());
    }

    @Test
    public void getCategoryTreeVoList() {
        List<CategoryTreeVo> categoryTreeVoList = adminService.getCategoryTreeVoList();
        Assert.assertNotEquals(0,categoryTreeVoList.size());

    }
}