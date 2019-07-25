package com.cumt.chiduoduo.model;

public class Category {
    private Integer id;

    private String name;

    private String imgurl;

    private String icon;

    private Integer highestCategoryId;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getHighestCategoryId() {
        return highestCategoryId;
    }

    public void setHighestCategoryId(Integer highestCategoryId) {
        this.highestCategoryId = highestCategoryId;
    }
}