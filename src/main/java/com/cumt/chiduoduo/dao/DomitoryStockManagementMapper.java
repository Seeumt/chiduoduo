package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.DomitoryStockManagement;

public interface DomitoryStockManagementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DomitoryStockManagement record);

    int insertSelective(DomitoryStockManagement record);

    DomitoryStockManagement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DomitoryStockManagement record);

    int updateByPrimaryKey(DomitoryStockManagement record);
}