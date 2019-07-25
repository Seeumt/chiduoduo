package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.OrderMaster;

import java.util.List;

public interface OrderMasterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderMaster record);

    int insertSelective(OrderMaster record);

    OrderMaster selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderMaster record);

    int updateByPrimaryKey(OrderMaster record);

  List<OrderMaster> selectByProductId(Integer productId);
  List<OrderMaster> selectByTotalorderSn(String totalorderSn);

    List<OrderMaster> selectByUserId(Integer userId);
}
