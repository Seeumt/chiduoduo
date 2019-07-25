package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.controller.Form.OrderForm;
import com.cumt.chiduoduo.controller.vo.ShoppingCartVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.ShoppingCart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：订单服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void addProductToShoppingCart() {
    }

    @Test
    public void getShoppingCart() throws BusinessException {
        List<ShoppingCartVO> shoppingCartVOList = orderService.getShoppingCart(20950);
        Assert.assertNotEquals(0,shoppingCartVOList.size());

    }
}