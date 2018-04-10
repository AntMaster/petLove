package com.shumahe.pethome.Service;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Form.PetSearchForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SearchService {

    /**
     * 宠物搜索
     * @param petSearchForm
     */
    List<PublishDTO> petSearch(PetSearchForm petSearchForm);

    /**
     * 后台搜索
     * @param petSearchForm
     * @return
     */
    Map<String, Object> adminPetSearch(String keywords,Integer adminPetSearch);

    List<PublishDTO> init();
}
