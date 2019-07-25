package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.Domitory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DomitoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Domitory record);

    int insertSelective(Domitory record);

    Domitory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Domitory record);

    int updateByPrimaryKey(Domitory record);

  Domitory selectByDomitoryNoAndBuildingId(@Param("domitoryNo") String domitoryNo, @Param("buildingId")Integer buildingId);

    List<Domitory> selectByBuildingId(Integer buildingId);
}
