package com.cumt.chiduoduo.convertor;

import com.cumt.chiduoduo.controller.vo.ProductListVO;
import com.cumt.chiduoduo.controller.vo.ShoppingCartVO;
import com.cumt.chiduoduo.model.*;
import com.cumt.chiduoduo.service.model.ProductListModel;
import com.cumt.chiduoduo.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：转换类，用于对层需要的各个对象进行转换
 *
 */
@Component
public class Convertor {
  public static Convertor convertor;

//  @Resource
//  private UserPasswordMapper userPasswordMapper;
//  @Resource
//  private BuildingMapper buildingMapper;
//  @Resource
//  private SchoolMapper schoolMapper;
//  @Autowired
//  private CategoryMapper categoryMapper;
  //用多个数据实体dataobject构建用户数据模型UserModel
  public  UserModel dataObject2UserModel(User user, UserPassword userPassword, Building building, School school) {
    UserModel userModel = new UserModel();
    BeanUtils.copyProperties(user, userModel);
    userModel.setLastResetPasswordTime(userPassword.getLastResetPasswordTime());
    userModel.setPassword(userPassword.getPassword());
    userModel.setBuildingName(building.getBuildingName());
    userModel.setSchoolName(school.getSchoolName());
    return userModel;
  }




  public ProductListVO productListModel2ProductListVO(ProductListModel productListModel) {
    ProductListVO productListVO = new ProductListVO();
    BeanUtils.copyProperties(productListModel,productListVO);
    return productListVO;
  }

  public List<ProductListVO> productListModelList2ProductListVOListConvert(List<ProductListModel> productListModelList) {
    return productListModelList.stream().map(e ->
      productListModel2ProductListVO(e)
    ).collect(Collectors.toList());
  }


}
