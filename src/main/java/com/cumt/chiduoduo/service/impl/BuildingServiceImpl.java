package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.dao.BuildingMapper;
import com.cumt.chiduoduo.dao.SchoolMapper;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.Building;
import com.cumt.chiduoduo.model.School;
import com.cumt.chiduoduo.service.BuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：宿舍楼服务层实现类
 *
 */
@Slf4j
@Service
public class BuildingServiceImpl implements BuildingService {

  @Resource
  private BuildingMapper buildingMapper;
  @Resource
  private SchoolMapper schoolMapper;

  SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

  /**
   * 通过学校编号查询学校宿舍楼信息
   * @param schoolId
   * @return
   * @throws BusinessException
   */
  @Override
  public List<Building> selectBuildingBySchoolId(Integer schoolId) throws BusinessException {
    List<Building> buildingList = buildingMapper.selectBySchoolId(schoolId);
    if (buildingList == null) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"有用户通过学校编号查询学校宿舍楼信息，无法找到该校所在编号，操作失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "无法找到该学校编号的宿舍楼");
    }
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"有用户通过学校编号查询学校宿舍楼信息，操作成功");
    return buildingList;
  }

  /**
   * 用户通过学校名称查询学校宿舍楼信息
   * @param schoolName
   * @return
   * @throws BusinessException
   */
  @Override
  public List<Building> selectBuildingBySchoolName(String schoolName) throws BusinessException {
    School school = schoolMapper.selectSchoolName(schoolName);
    if (school == null) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"有用户通过学校名称查询学校宿舍楼信息，无法找到用户所输入的学校，操作失败");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "学校名称传参有误");
    }
    List<Building> buildingList = selectBuildingBySchoolId(school.getId());
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"有用户通过学校名称查询学校宿舍楼信息，成功找到学校名称为"+schoolName+"的学校的宿舍信息");
    return buildingList;
  }
}
