package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.UserOnlineStatistics;

import java.util.List;

public interface UserOnlineStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOnlineStatistics record);

    int insertSelective(UserOnlineStatistics record);

    UserOnlineStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOnlineStatistics record);

    int updateByPrimaryKey(UserOnlineStatistics record);

    List<UserOnlineStatistics> selectOnlineUser(String userLoginStatus);

    UserOnlineStatistics selectByUserId(Integer userId);
}