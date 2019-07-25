package com.cumt.chiduoduo.controller.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：订单评论表单
 *
 */

@Data
public class OrderCommentForm {
    @NotBlank(message = "大订单编号不能为空")
    private String totalorderSn;
    @NotNull(message = "小订单id不能为空")
    private Integer orderMasterId;

    @NotNull(message = "质量打分不能为空")
    @Max(value = 5,message = "打分不能高于5")
    private Integer quality;

    @NotNull(message = "口味打分不能为空")
    @Max(value = 5,message = "打分不能高于5")
    private Integer taste;

    @NotNull(message = "服务打分不能为空")
    @Max(value = 5,message = "打分不能高于5")
    private Integer service;

    @NotBlank(message = "订单文字不能为空")
    private String comment;

    private MultipartFile img1=null;

    private MultipartFile img2=null;

    private MultipartFile img3=null;
}
