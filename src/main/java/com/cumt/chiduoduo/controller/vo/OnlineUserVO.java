package com.cumt.chiduoduo.controller.vo;

import lombok.Data;

import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户在线统计展示类
 *
 */

@Data
public class OnlineUserVO {
    private Integer userId;

    private String login;

    private String loginTime;
}
