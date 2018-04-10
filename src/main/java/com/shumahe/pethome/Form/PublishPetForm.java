package com.shumahe.pethome.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class PublishPetForm {


    @NotBlank(message = "宠物昵称必填")
    private String petName;



    @NotNull(message = "宠物性别必填")
    private int petSex;


    @NotNull(message = "宠物类别必填")
    private int classifyId;



    @NotNull(message = "宠物品种必填")
    private int varietyId;


    @NotBlank(message = "丢失日期必填")
    private String lostTime;


    @NotBlank(message = "丢失地点必填")
    private String lostLocation;


    @NotNull(message = "地点经度必填")
    private float latitude;


    @NotNull(message = "地点纬度必填")
    private float longitude;


    @NotBlank(message = "主人姓名必填")
    private String ownerName;


    @NotBlank(message = "联系方式必填")
    private String ownerContact;


    @NotEmpty(message = "图片必填")
    private String petImage;


    private String petDescription;

    @NotBlank(message = "发布人主键必填")
    private String publisherId;

}
