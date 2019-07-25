package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.User;
import com.cumt.chiduoduo.model.UserPassword;
import com.cumt.chiduoduo.service.model.UserModel;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户服务接口
 *
 */
public interface UserService {
  UserModel getUserById(Integer id);
  /*用户注册*/
  void register(UserModel userModel) throws BusinessException;

  /*检测用户密码重置接口传进来的邮箱是否在数据库中存在*/
  boolean validateEmail(String email) throws BusinessException;

//  /*验证用户登录情况
//      telphone:用户注册手机 password:用户加密后的密码*/
//  UserModel validateLogin(String telphone, String password) throws BusinessException;

  /*通过邮箱查找用户*/
  User selectByEmail(String email) throws BusinessException;

  UserModel getByEmail(String email) throws BusinessException;
  /*通过用户id查找用户密码*/
  UserPassword selectByUserId(Integer userId) throws BusinessException;
/*重置密码*/
  void resetPassword(String email,String password) throws BusinessException;


}
