package com.cumt.chiduoduo.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderMaster {
    private Integer id;

    private String totalorderSn;

    private Integer userId;

    private Integer productId;

    private Integer amount;

    private BigDecimal orderPrice;

    private String comment;

    private Date commentTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTotalorderSn() {
        return totalorderSn;
    }

    public void setTotalorderSn(String totalorderSn) {
        this.totalorderSn = totalorderSn == null ? null : totalorderSn.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}