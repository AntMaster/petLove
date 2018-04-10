package com.shumahe.pethome.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class UserApproveForm {


    @NotEmpty(message = "用户主键不能为空")
    private String userId;


    @NotEmpty(message = "组织或协会名称不能为空")
    private String organizationName;


    @NotEmpty(message = "图片不能为空")
    private String organizationImage;


    @NotEmpty(message = "负责人不能为空")
    private String dutyer;


    @NotEmpty(message = "联系方式不能为空")
    private String dutyerPhone;

    @NotNull(message = "验证码必填")
    private Integer messageCode;


}
