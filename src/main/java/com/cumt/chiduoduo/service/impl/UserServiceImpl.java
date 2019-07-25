package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.convertor.Convertor;
import com.cumt.chiduoduo.dao.*;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.service.UserService;
import com.cumt.chiduoduo.service.model.UserModel;
import com.cumt.chiduoduo.validator.ValidationResult;
import com.cumt.chiduoduo.validator.ValidatorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户相关服务层实现类
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Resource
  private UserMapper userMapper;
  @Resource
  private UserPasswordMapper userPasswordMapper;
  @Autowired
  private ValidatorImpl validator;
  @Resource
  private BuildingMapper buildingMapper;
  @Resource
  private SchoolMapper schoolMapper;
  @Autowired
  private Convertor convertor;
  @Resource
  private DomitoryMapper domitoryMapper;

  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * 通过用户id获取用户信息
   * @param id
   * @return
   */
  @Override
  public UserModel getUserById(Integer id) {
    User user = userMapper.selectByPrimaryKey(id);
    if (user == null) {
      return null;
    }
    UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
    Building building = buildingMapper.selectByPrimaryKey(user.getBuildingId());
    School school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
    return convertor.dataObject2UserModel(user, userPassword,building,school);
  }

  /**
   * 对用户注册时的传参里面的各个参数校验
   * @param userModel
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void register(UserModel userModel) throws BusinessException {
    ValidationResult validationResult=validator.validate(userModel);
    if (validationResult.isHasErrors()) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"有用户注册账号，但注册时所传递的参数出错");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,
        validationResult.getErrMsg()+"注册时所传递的参数出错");
    }
    //入库用户表
    User user = new User();
    BeanUtils.copyProperties(userModel, user);
    try {
      userMapper.insertSelective(user);
    } catch (DuplicateKeyException e) {//用于监测数据库中用户手机号/邮箱号是否唯一时的报错
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"邮箱为"+userModel.getEmail()+"的用户通过已注册过的邮箱注册账号，注册失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "手机号/邮箱号已注册");
    }
    userModel.setId(user.getId());
    //入库用户密码表
    UserPassword userPassword = new UserPassword();
    BeanUtils.copyProperties(userModel, userPassword);
    userPassword.setUserId(userModel.getId());
    userPasswordMapper.insertSelective(userPassword);
    //查找该用户的宿舍是否已经在数据库中
    String domitoryNo=userModel.getDomitoryNo();
    Integer buildingId=userModel.getBuildingId();
    Domitory domitory=domitoryMapper.selectByDomitoryNoAndBuildingId(domitoryNo,buildingId);
    //入库用户宿舍信息表
    if (domitory == null) {
      Domitory domitory1 = new Domitory();
      domitory1.setBuildingId(buildingId);
      domitory1.setNumber(domitoryNo);
      domitoryMapper.insertSelective(domitory1);
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"将新注册的宿舍号为"+domitoryNo+"的用户宿舍号存入宿舍数据库");
    }
  }

  /**
   * 解析邮件
   * @param email
   * @return
   * @throws BusinessException
   */
  @Override
  public boolean validateEmail(String email) throws BusinessException {
    User user = userMapper.selectByEmail(email);
    if (user == null) {
      return false;
    }
    return true;
  }

  /**
   * 通过邮箱验证用户是否存在
   * @param email
   * @return
   * @throws BusinessException
   */
  @Override
  public User selectByEmail(String email) throws BusinessException {
    User user = userMapper.selectByEmail(email);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "使用该邮箱注册的用户不存在");
    }
    return user;
  }

  /**
   * 通过邮箱获取用户
   * @param email
   * @return
   * @throws BusinessException
   */
  @Override
  public UserModel getByEmail(String email) throws BusinessException {
    User user = userMapper.selectByEmail(email);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "使用该邮箱注册的用户不存在");
    }
    UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
    School school = schoolMapper.selectByPrimaryKey(user.getSchoolId());
    Building building = buildingMapper.selectByPrimaryKey(user.getBuildingId());
    UserModel userModel=convertor.dataObject2UserModel(user, userPassword,building,school);
    return userModel;
  }

  /**
   * 通过邮箱验证用户密码是否存在
   * @param userId
   * @return
   * @throws BusinessException
   */
  @Override
  public UserPassword selectByUserId(Integer userId) throws BusinessException {
    UserPassword userPassword = userPasswordMapper.selectByUserId(userId);
    if (userPassword == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "使用该邮箱注册的用户密码不存在");
    }
    return userPassword;
  }

  /**
   * 通过邮箱判断用户是否存在，修改的密码是否与原密码相同，密码是否修改成功
   * @param email
   * @param password
   * @throws BusinessException
   */
  @Override
  @Transactional
  public void resetPassword(String email, String password) throws BusinessException {
    User user = userMapper.selectByEmail(email);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
    }
    UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
    //检测新密码是否与旧密码相同
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    if (bCryptPasswordEncoder.matches(password, userPassword.getPassword())) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"————"+"编号为："+user.getId()+"的用户修改的新密码与原密码相同，修改失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "不允许新密码与旧密码相同");
    }
    userPassword.setPassword(bCryptPasswordEncoder.encode(password));
    userPassword.setLastResetPasswordTime(new Date());
    userPasswordMapper.updateByPrimaryKeySelective(userPassword);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"————"+"编号为："+user.getId()+"的用户修改密码成功");
  }


}
