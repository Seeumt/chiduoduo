package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.WishList;
import io.swagger.models.auth.In;

import java.util.List;

public interface WishListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WishList record);

    int insertSelective(WishList record);

    WishList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WishList record);

    int updateByPrimaryKey(WishList record);

  List<WishList> selectByUserId(Integer userId);

  List<WishList> selectByDomitoryId(Integer domitoryId);
}
