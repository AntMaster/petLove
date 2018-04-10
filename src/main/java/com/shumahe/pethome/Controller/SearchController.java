package com.shumahe.pethome.Controller;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.PetSearchForm;
import com.shumahe.pethome.Service.SearchService;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/search")
@CrossOrigin
public class SearchController {


    @Autowired
    SearchService searchService;

    /**
     * 宠物搜索
     *
     * @param petSearchForm
     * @return
     */
    @GetMapping("/")
    public ResultVO petSearch(@Valid PetSearchForm petSearchForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.error("【搜索参数不正确】参数不正确,petForm={}", bindingResult);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        List<PublishDTO> publishDTOS = searchService.petSearch(petSearchForm);
        return ResultVOUtil.success(publishDTOS);
    }


    @GetMapping("/init")
    public ResultVO searchInit() {

        List<PublishDTO> publishDTOS = searchService.init();
        return ResultVOUtil.success(publishDTOS);
    }





}
