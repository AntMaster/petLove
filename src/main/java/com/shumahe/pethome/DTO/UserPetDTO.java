package com.shumahe.pethome.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shumahe.pethome.Domain.UserPetAlbum;
import com.shumahe.pethome.Enums.ContraceptionStateEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserPetDTO {

    private Integer id;


    private String headImgUrl;


    private String nickName;


    private Integer classifyId;

    private String classifyName;

    private Integer varietyId;

    private String varietyName;

    private Integer sex;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;

    private Integer contraception;

    private String description;


    private String chipNo;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;


    /**
     * 相册数量
     */
    private Integer albumCount = 0;

    List<UserPetAlbumDTO> petAlbumDTOS;


}
