package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //获取类别列表
    List<Category> selectAllCategorys();
    Category selectById(Integer id);
}