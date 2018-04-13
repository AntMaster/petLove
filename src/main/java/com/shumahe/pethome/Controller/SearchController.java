package com.shumahe.pethome.Controller;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.PetSearchForm;
import com.shumahe.pethome.Service.SearchService;
import com.shumahe.pethome.Service.UserService;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/search")
@CrossOrigin
public class SearchController {


    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

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


    @GetMapping("/init/{openid}")
    public ResultVO searchInit(@PathVariable("openid") String openid) {

        List<PublishDTO> publishDTOS = searchService.init();

        Map<String,Object>  res = new HashMap<>();
        res.put("list",publishDTOS);
        res.put("showMsgPoint",userService.isShowMsgPoint(openid));
        return ResultVOUtil.success(res);
    }





}
