package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.HighestCategory;

import java.util.List;

public interface HighestCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HighestCategory record);

    int insertSelective(HighestCategory record);

    HighestCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HighestCategory record);

    int updateByPrimaryKey(HighestCategory record);

    List<HighestCategory> selectAllHighestCategory();
}