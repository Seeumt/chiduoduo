package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.model.Building;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：宿舍楼服务接口
 *
 */
public interface BuildingService {

  List<Building> selectBuildingBySchoolId(Integer schoolId) throws BusinessException;
  List<Building> selectBuildingBySchoolName(String  schoolName) throws BusinessException;

}
