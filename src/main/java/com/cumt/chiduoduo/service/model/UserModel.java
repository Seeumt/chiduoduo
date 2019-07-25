package com.cumt.chiduoduo.service.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户类服务层模型
 *
 */
@Data
public class UserModel {
  private Integer id;
  @NotBlank(message = "用户名不能为空")
  private String userName;
  @NotNull(message = "楼层编号不能为空")
  private Integer buildingId;
  @NotBlank(message = "学校编号不能为空")
  private Integer schoolId;
  @NotBlank(message = "用户邮箱不能为空")
  private String email;
  //目前的前端没有让用户写手机，手机允许为空
  private String telphone;
  @NotBlank(message = "用户密码不能为空")
  private String password;
  @NotBlank(message = "最后密码更新时间不能为空")
  private Date lastResetPasswordTime;
  private String buildingName;
  private String schoolName;
  @NotBlank(message = "宿舍号不能为空")
  private String domitoryNo;

}
