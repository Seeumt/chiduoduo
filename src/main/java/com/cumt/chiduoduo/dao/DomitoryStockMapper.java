package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.DomitoryStock;

import java.util.List;

public interface DomitoryStockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DomitoryStock record);

    int insertSelective(DomitoryStock record);

    DomitoryStock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DomitoryStock record);

    int updateByPrimaryKey(DomitoryStock record);

  List<DomitoryStock> selectByProductId(Integer productId);

  boolean deleteByProductId(Integer productId);

  List<DomitoryStock> selectByDomitoryId(Integer domitoryId);
}
