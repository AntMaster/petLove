package com.shumahe.pethome.Form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class UserPetAlbumForm {


    @NotNull(message = "宠物主键petId必填")
    private Integer petId;


    @NotBlank(message = "相册名称name必填")
    private String name;


    private String description;

}
