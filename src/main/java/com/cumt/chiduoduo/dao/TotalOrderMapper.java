package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.TotalOrder;

import java.util.List;

public interface TotalOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TotalOrder record);

    int insertSelective(TotalOrder record);

    TotalOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TotalOrder record);

    int updateByPrimaryKey(TotalOrder record);

  List<TotalOrder> selectByDomitoryNo(String domitoryNo);
}
