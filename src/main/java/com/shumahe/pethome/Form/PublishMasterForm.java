package com.shumahe.pethome.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class PublishMasterForm {


    @NotBlank(message = "宠物图片必填")
    private String petImage;


    @NotNull(message = "宠物类别必填")
    private int classifyId;


    private int varietyId;

    @NotBlank(message = "发现时间必填")
    private String lostTime;


    @NotBlank(message = "发现地点必填")
    private String lostLocation;


    @NotNull(message = "地点经度必填")
    private float latitude;


    @NotNull(message = "地点纬度必填")
    private float longitude;


    @NotBlank(message = "发现人必填")
    private String ownerName;


    @NotBlank(message = "联系方式必填")
    private String ownerContact;


    @NotBlank(message = "发布人主键必填")
    private String publisherId;


    private String petDescription;

    /*@NotBlank(message = "openId必填")
    private String openId;*/

}
