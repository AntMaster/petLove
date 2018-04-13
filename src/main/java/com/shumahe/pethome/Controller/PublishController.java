package com.shumahe.pethome.Controller;


import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.PetVariety;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.PublishMasterForm;
import com.shumahe.pethome.Form.PublishPetForm;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Service.BaseService.PublishBaseService;
import com.shumahe.pethome.Service.PublishService;
import com.shumahe.pethome.Service.UserService;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.PublishVO;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publish")
@Slf4j
public class PublishController {


    @Autowired
    PublishService publishService;

    @Autowired
    UserService userService;

    /**
     * 首页模块 ------> 动态 + 寻主 + 寻宠 三个列表
     *
     * @param publishType
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/index/{openId}")
    public ResultVO publishList(@PathVariable(name = "openId") String openId,
                                @RequestParam(value = "publishType", defaultValue = "0") Integer publishType,
                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "size", defaultValue = "100") Integer size) {

        PageRequest request = new PageRequest(page, size);
        List<PublishDTO> pubList = publishService.findAll(openId, publishType, request);

        //是否有消息显示
        boolean showMsgPoint = userService.isShowMsgPoint(openId);

        Map<String, Object> res = new HashMap<>();
        res.put("list", pubList);
        res.put("isShowMsgPoint", showMsgPoint);
        return ResultVOUtil.success(res);

    }

    /**
     * 发布模块 ------> 寻宠发布
     *
     * @param petForm
     * @param bindingResult
     * @return
     */

    /**
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * @param  [参数1] [in或out]  [参数1说明]
     * @param  [参数2] [in或out]  [参数2说明]
     * @return [返回类型说明]
     * @exception/throws [违例类型] [违例说明]<code></code>
     */
    @PutMapping("/pet/{openId}")
    public ResultVO<Map<String, String>> create(@Valid PublishPetForm petForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.error("【发布寻宠】参数不正确,petForm={}", petForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        //查询结果
        PetPublish petPublish = publishService.createPet(petForm);

        //组装返回数据
        Map<String, String> returnData = new HashMap<>();
        returnData.put("openId", petPublish.getPublisherId());
        returnData.put("publishId", String.valueOf(petPublish.getId()));
        return ResultVOUtil.success(returnData);
    }

    /**
     * 发布模块 ------> 寻主发布
     *
     * @param masterForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/master/{openId}")
    public ResultVO<Map<String, String>> create(@Valid PublishMasterForm masterForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【发布寻主】参数不正确,petForm={}", masterForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        //查询结果
        PetPublish petPublish = publishService.createMaster(masterForm);

        //组装返回数据
        Map<String, String> returnData = new HashMap<>();
        returnData.put("openId", petPublish.getPublisherId());
        returnData.put("publishId", String.valueOf(petPublish.getId()));

        return ResultVOUtil.success(returnData);

    }


    /**
     * 我的模块 ------> 发布列表
     * PS:按时间查个人
     */
    @GetMapping("/{openId}")
    public ResultVO myPublish(@PathVariable("openId") String openId,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        List<PublishDTO> myPublishList = publishService.findMyPublishList(openId, pageRequest);
        return ResultVOUtil.success(myPublishList);
    }


    /**
     * 我的模块 ------> 待处理列表
     * PS:按时间查个人
     */
    @GetMapping("/task/{openId}")
    public ResultVO myNotFound(@PathVariable("openId") String openId,
                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        List<PublishDTO> myPublishList = publishService.findNotFound(openId, pageRequest);


        return ResultVOUtil.success(myPublishList);
    }


    /**
     * 详情模块 ------> 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{openId}")
    public ResultVO petDetail(@PathVariable("openId") String openId, @RequestParam("id") Integer id) {

        if (id == 0) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }
        PublishDTO petDetail = publishService.findPetDetail(id, openId);
        return ResultVOUtil.success(petDetail);
    }


    /**
     * 详情模块 ------> 已找到
     *
     * @param id
     * @param openId
     * @return
     */
    @PostMapping("pet/find/{openId}")
    public ResultVO petFind(@PathVariable("openId") String openId, @RequestParam("id") Integer id) {

        PetPublish petPublish = publishService.petFound(openId, id);
        return ResultVOUtil.success(petPublish);
    }

    @Autowired
    PublishBaseService publishBaseService;

    @GetMapping("test")
    public ResultVO test() {

        Map<Integer, Map<Integer, PetVariety>> petVariety = publishBaseService.findPetVariety();
        return ResultVOUtil.success(petVariety);

    }

}
