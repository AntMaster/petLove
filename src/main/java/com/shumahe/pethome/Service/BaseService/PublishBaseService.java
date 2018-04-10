package com.shumahe.pethome.Service.BaseService;


import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.PetVariety;

import java.util.List;
import java.util.Map;


public interface PublishBaseService {

    /**
     * 查询发布扩展信息(发布人详情 + 评论数详情)
     *
     * @param petPublishes
     * @return
     */
    List<PublishDTO> findPetExtends(List<PetPublish> petPublishes);


    Integer findPublishView(String openId,PetPublish petPublish);


    Map<Integer,Map<Integer,PetVariety>> findPetVariety();


    /**
     * 获取发布详情信息
     * @param publishes
     * @return
     */
    List<PublishDTO> findPetsDetail(List<PetPublish> publishes);


}
