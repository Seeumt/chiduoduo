package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.School;

import java.util.List;

public interface SchoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(School record);

    int insertSelective(School record);

    School selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(School record);

    int updateByPrimaryKey(School record);

  List<School> selectAllSchools();

  School selectSchoolName(String schoolName);
}
