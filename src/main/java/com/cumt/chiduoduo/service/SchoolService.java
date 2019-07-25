package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.model.School;

import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：学校服务接口
 *
 */
public interface SchoolService {
  List<School> selectAllSchools();

}
