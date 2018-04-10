package com.shumahe.pethome.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shumahe.pethome.Domain.UserPetPhoto;
import lombok.Data;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserPetAlbumDTO {


    private Integer id;

    private String name;

    private Integer classifyId;

    private String coverPath;

    private String description;

    private Integer photoCount = 0;

    private List<UserPetPhotoDTO> petPhotoDTOS;

}
