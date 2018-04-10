package com.shumahe.pethome.Convert;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Publish2PublishDTOConvert {

    public static PublishDTO convert(PetPublish publish) {

        PublishDTO publishDTO = new PublishDTO();
        BeanUtils.copyProperties(publish, publishDTO);
        return publishDTO;

    }

    public static List<PublishDTO> convert(List<PetPublish> publishes) {

        return publishes.stream().map(publish -> convert(publish)).collect(Collectors.toList());
    }

}
