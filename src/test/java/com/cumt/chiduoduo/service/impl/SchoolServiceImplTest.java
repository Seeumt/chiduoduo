package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.model.School;
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
 * @description：学校服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolServiceImplTest {
    @Autowired
    private SchoolServiceImpl schoolService;

    @Test
    public void selectAllSchools() {
        List<School> schoolList=schoolService.selectAllSchools();
        Assert.assertNotEquals(0,schoolList.size());
    }
}