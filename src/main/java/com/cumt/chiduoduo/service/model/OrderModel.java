package com.cumt.chiduoduo.service.model;

import com.cumt.chiduoduo.model.OrderMaster;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：订单类服务层模型
 *
 */
@Data
public class OrderModel {
  private Integer id;

  private String orderSn;

  private BigDecimal totalPrice;

  private Integer userId;

  private Integer payStatus;

  private String userDomitoryno;

  private Date createTime;

  private Date updateTime;

  List<OrderMaster> orderMasterList;
}
