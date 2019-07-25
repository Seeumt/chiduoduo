package com.cumt.chiduoduo.model;

public class WishList {
    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer userDomitoryid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserDomitoryid() {
        return userDomitoryid;
    }

    public void setUserDomitoryid(Integer userDomitoryid) {
        this.userDomitoryid = userDomitoryid;
    }
}