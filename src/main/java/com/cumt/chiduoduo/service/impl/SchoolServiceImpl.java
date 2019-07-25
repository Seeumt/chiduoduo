package com.cumt.chiduoduo.service.impl;

import com.cumt.chiduoduo.dao.SchoolMapper;
import com.cumt.chiduoduo.model.School;
import com.cumt.chiduoduo.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：学校服务层实现类
 *
 */
@Slf4j
@Service
public class SchoolServiceImpl implements SchoolService {
  @Resource
  private SchoolMapper schoolMapper;

  SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

  /**
   * 用户查询所有学校的信息
   * @return
   */
  @Override
  public List<School> selectAllSchools() {
    List<School> schoolList = schoolMapper.selectAllSchools();
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"有用户查询所有学校的信息");
    return schoolList;
  }
}
