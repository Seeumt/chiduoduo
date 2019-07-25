package com.cumt.chiduoduo.service.model;

import lombok.Data;

import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：评论类服务层模型
 *
 */
@Data
public class CommentModel {


  private String userName;
  private Integer productId;
  private String comment;
  private Date commentTime;
}
