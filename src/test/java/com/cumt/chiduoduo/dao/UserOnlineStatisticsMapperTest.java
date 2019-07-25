package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.UserOnlineStatistics;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户登录持久层数据测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserOnlineStatisticsMapperTest {
    @Resource
    private UserOnlineStatisticsMapper userOnlineStatisticsMapper;

    public void selectOnlineUser() {
//        List<UserOnlineStatistics> userOnlineStatisticsList = userOnlineStatisticsMapper.selectOnlineUser("在线");


    }
}