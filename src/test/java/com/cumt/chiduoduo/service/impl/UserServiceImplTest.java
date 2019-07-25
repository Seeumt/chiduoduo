package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.UserPassword;
import com.cumt.chiduoduo.service.model.UserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    public void getUserById() {
        UserModel userModel = userService.getUserById(20950);
        Assert.assertEquals(20950,(long)userModel.getId());
    }

    @Test
    public void register() throws BusinessException {
        UserModel userModel = new UserModel();
        userModel.setId(20954);
        userModel.setBuildingId(1);
        userModel.setSchoolId(1);
        userModel.setDomitoryNo("A4011");
        userModel.setEmail("123456767@qq.com");
        userModel.setTelphone("123456789098");
        userModel.setUserName("张三");
        userService.register(userModel);
    }

    @Test
    public void getByEmail() throws BusinessException {
        UserModel userModel = userService.getByEmail("5514@qq.com");
        Assert.assertEquals("5514@qq.com",userModel.getEmail());
    }

    @Test
    public void selectByUserId() throws BusinessException {
        UserPassword userPassword = userService.selectByUserId(20950);
        Assert.assertEquals(20950,(long)userPassword.getUserId());
    }
}