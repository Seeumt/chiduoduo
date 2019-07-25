package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Building;
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
 * @description：宿舍服务结构实现测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildingServiceImplTest {
    @Autowired
    private BuildingServiceImpl buildingService;

    @Test
    public void selectBuildingBySchoolId() throws BusinessException {
        List<Building> buildingList = buildingService.selectBuildingBySchoolId(1);
        Assert.assertNotEquals(0,buildingList.size());
    }

    @Test
    public void selectBuildingBySchoolName() throws BusinessException {
        List<Building> buildingList = buildingService.selectBuildingBySchoolName("中国矿业大学");
        Assert.assertNotEquals(0,buildingList.size());
    }
}