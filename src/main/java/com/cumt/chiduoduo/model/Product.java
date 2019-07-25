package com.cumt.chiduoduo.model;

import java.math.BigDecimal;

public class Product {
    private Integer id;

    private String name;

    private String description;

    private Integer categoryId;

    private BigDecimal price;

    private Integer stock;

    private Integer sales;

    private String imgNo1;

    private String imgNo2;

    private String imgNo3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgNo1() {
        return imgNo1;
    }

    public void setImgNo1(String imgNo1) {
        this.imgNo1 = imgNo1 == null ? null : imgNo1.trim();
    }

    public String getImgNo2() {
        return imgNo2;
    }

    public void setImgNo2(String imgNo2) {
        this.imgNo2 = imgNo2 == null ? null : imgNo2.trim();
    }

    public String getImgNo3() {
        return imgNo3;
    }

    public void setImgNo3(String imgNo3) {
        this.imgNo3 = imgNo3 == null ? null : imgNo3.trim();
    }
}