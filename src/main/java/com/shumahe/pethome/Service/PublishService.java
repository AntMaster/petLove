package com.shumahe.pethome.Service;


import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Form.PetSearchForm;
import com.shumahe.pethome.Form.PublishMasterForm;
import com.shumahe.pethome.Form.PublishPetForm;
import com.shumahe.pethome.VO.PublishVO;
import com.shumahe.pethome.VO.ResultVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PublishService {

    /**
     * 主页列表(动态+寻主+寻宠)
     *
     * @param publishType
     * @param pageable
     * @return
     */
    List<PublishDTO> findAll(String openId,Integer publishType, PageRequest pageable);

    /**
     * 寻宠发布
     *
     * @param petForm
     * @return
     */
    PetPublish createPet(PublishPetForm petForm);

    /**
     * 寻主发布
     *
     * @param masterForm
     * @return
     */
    PetPublish createMaster(PublishMasterForm masterForm);

    /**
     * 我的发布列表
     *
     * @param openId
     * @param pageable
     * @return
     */
    List<PublishDTO> findMyPublishList(String openId, PageRequest pageable);

    /**
     * 我的待处理列表
     *
     * @param openId
     * @param pageRequest
     * @return
     */
    List<PublishDTO> findNotFound(String openId, PageRequest pageRequest);


    /**
     * 宠物详情(发布+互动)
     *
     * @param publishId
     * @param openId
     * @return
     */
    PublishDTO findPetDetail(Integer publishId, String openId);



    /**
     * 已找到
     *
     * @param id)
     * @param openId
     * @return
     */
    PetPublish petFound(String openId,Integer id);




}
