package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 21:54
 * @description：订单评论返回数据
 * @modified By：
 * @version: 1$
 */
@Data
public class OrderCommentVO {
    private String userName;

    private Integer quality;
    private Integer taste;
    private Integer service;
    private String comment;
    private String  createTime;

    private String imgNo1;

    private String imgNo2;

    private String imgNo3;

}
