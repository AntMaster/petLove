package com.shumahe.pethome.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class UserPetPhotoForm {


    @NotNull(message = "相册主键albumId必填")
    private Integer albumId;


    private String name;

    @NotBlank(message = "照片路径path必填")
    private String path;


    private String description;



}
