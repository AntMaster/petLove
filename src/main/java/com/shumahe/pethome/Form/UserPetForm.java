package com.shumahe.pethome.Form;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class UserPetForm {


    private String userId;


    @NotNull(message = "爱宠类别必填")
    private Integer classifyId;


    private Integer varietyId;


    @NotBlank(message = "爱宠头像必填")
    private String headImgUrl;


    @NotBlank(message = "爱宠昵称必填")
    private String nickName;


    @NotNull(message = "爱宠节育状态必填")
    private Integer contraception;


    @NotNull(message = "爱宠性别必填")
    private Integer sex;


    @NotBlank(message = "爱宠生日必填")
    private String birthday;


    @NotBlank(message = "爱宠小特点必填")
    private String description;


    private String chipNo;

}
