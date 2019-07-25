package com.cumt.chiduoduo.model;

public class DomitoryStock {
    private Integer id;

    private Integer domitoryId;

    private Integer productId;

    private String productName;

    private Integer domitoryStock;

    private Integer categoryId;

    private String categoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDomitoryId() {
        return domitoryId;
    }

    public void setDomitoryId(Integer domitoryId) {
        this.domitoryId = domitoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getDomitoryStock() {
        return domitoryStock;
    }

    public void setDomitoryStock(Integer domitoryStock) {
        this.domitoryStock = domitoryStock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }
}