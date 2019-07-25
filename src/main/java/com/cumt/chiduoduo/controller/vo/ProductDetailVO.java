package com.cumt.chiduoduo.controller.vo;

import com.cumt.chiduoduo.service.model.CommentModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：商品详情页面展示类
 *
 */

@Data
public class ProductDetailVO {
  private Integer productId;
  //商品名字
  private String ProductName;

  private String description;

  private Integer categoryId;

  private String categoryName;

  private BigDecimal price;

  private Integer sales;

  private String imgNo1;
  private String imgNo2;

  private String imgNo3;
  private String specification;

  private List<OrderCommentVO> orderCommentVOList;
  ;
}
