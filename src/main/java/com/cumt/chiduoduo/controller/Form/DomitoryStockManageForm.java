package com.cumt.chiduoduo.controller.Form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：宿舍商品管理表单
 *
 */

@Data
public class DomitoryStockManageForm {
  @NotEmpty
  private Integer productId;
  @NotEmpty
  private String ProductName;
  @NotEmpty
  private String description;
  @NotEmpty
  private Integer categoryId;
  @NotEmpty
  private String categoryName;
  @NotEmpty
  private BigDecimal price;
  @NotEmpty
  private Integer sales;
  @NotEmpty
  private String imgNo1;
  @NotEmpty
  private String specification;
  @NotEmpty
  private Integer wishListId;
  @NotEmpty
  private Integer changeAmount;
}
