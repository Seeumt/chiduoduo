package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.Building;
import io.swagger.models.auth.In;

import java.util.List;

public interface BuildingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Building record);

    int insertSelective(Building record);

    Building selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Building record);

    int updateByPrimaryKey(Building record);

  List<Building> selectBySchoolId(Integer schoolId);
}
