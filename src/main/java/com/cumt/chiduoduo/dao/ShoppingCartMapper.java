package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.ShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShoppingCart record);

    int insertSelective(ShoppingCart record);

    ShoppingCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);
    ShoppingCart selectByUserIdAndProductId(@Param("userId") Integer userId,
                                            @Param("productId") Integer productId);

    List<ShoppingCart> selectByUserId(Integer userId);
}