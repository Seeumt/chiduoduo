package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
  User selectByEmail(String email);

    List<User> selectAllUsers();

    User selectByUserName(String userName);
}
