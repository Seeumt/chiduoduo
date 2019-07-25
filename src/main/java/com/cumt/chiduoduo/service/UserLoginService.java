package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.dao.UserMapper;
import com.cumt.chiduoduo.dao.UserOnlineStatisticsMapper;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.User;
import com.cumt.chiduoduo.model.UserOnlineStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户登录服务接口
 *
 */
@Service
public class UserLoginService {

    @Resource
    private UserOnlineStatisticsMapper userOnlineStatisticsMapper;
    @Resource
    private UserMapper userMapper;

    public boolean userLogin(Integer userId) throws BusinessException {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        UserOnlineStatistics userOnlineStatistics = new UserOnlineStatistics();
        userOnlineStatistics.setLogin("在线");
        userOnlineStatistics.setLoginTime(new Date());
        userOnlineStatistics.setUserId(userId);
        UserOnlineStatistics userOnlineStatistics1 = userOnlineStatisticsMapper.selectByUserId(userId);
        if (userOnlineStatistics1 == null) {
            userOnlineStatisticsMapper.insertSelective(userOnlineStatistics);
        } else {
            userOnlineStatistics.setId(userOnlineStatistics1.getId());
            userOnlineStatisticsMapper.updateByPrimaryKeySelective(userOnlineStatistics);
        }
        return true;
    }

    //用户退出登录
    @Transactional
    public boolean userLogout(Integer userId) throws BusinessException {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户不合法，非注册用户");
        }
        UserOnlineStatistics userOnlineStatistics = userOnlineStatisticsMapper.selectByUserId(userId);
        if (userOnlineStatistics == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户并未曾登录");
        }
        userOnlineStatisticsMapper.deleteByPrimaryKey(userOnlineStatistics.getId());
        return true;
    }
}
