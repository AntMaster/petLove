package com.shumahe.pethome.Controller;


import com.shumahe.pethome.Domain.*;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.PetVarietyRepository;
import com.shumahe.pethome.Service.AdminService;
import com.shumahe.pethome.Service.Impl.SearchServiceImpl;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    PetVarietyRepository petVarietyRepository;

    /**
     * 寻宠 寻主 列表
     *
     * @param publishType
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/publish")
    public ResultVO findPublishList(@RequestParam(value = "publishType", defaultValue = "0") Integer publishType,
                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {

        if (publishType == 0) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }

        PageRequest request = new PageRequest(page, size);
        Map<String, Object> all = adminService.findAll(publishType, request);
        return ResultVOUtil.success(all);
    }


        /**
         *
         * @Description:
         * @Author:         zhangy
         * @CreateDate:     2018/4/12 19:01
         * @UpdateUser:
         * @UpdateDate:     2018/4/12 19:01
         * @UpdateRemark:   The modified content
         */
    @PostMapping("/publish/{id}")
    public ResultVO modifyShowState(@PathVariable("id") Integer id, @RequestParam("publishState") Integer publishState) {

        if (publishState != 0 && publishState != 1) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }

        PetPublish petPublish = adminService.modifyShowState(id, publishState);
        return ResultVOUtil.success(petPublish);
    }

    /**
     * 转发 关注
     */
    @GetMapping("/forward/{id}")
    public ResultVO findDynamic(@PathVariable("id") Integer id,
                                @RequestParam(value = "dynamicType", defaultValue = "0") Integer dynamicType,
                                @RequestParam(value = "day", defaultValue = "0") Integer day) {

        if (dynamicType == 0) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }

        Map<String, Object> dynamic = adminService.findDynamic(id, dynamicType, day);
        return ResultVOUtil.success(dynamic);
    }


    /**
     * 私信
     */
    @GetMapping("/private/{id}")
    public ResultVO findPrivateMsg(@PathVariable("id") Integer id,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "100") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        Map<String, Object> privateMsg = adminService.findPrivateMsg(id, pageRequest);
        return ResultVOUtil.success(privateMsg);

    }

    /**
     * 显示 隐藏 私信
     */
    @PostMapping("/private/{id}")
    public ResultVO modifyPrivateShow(@PathVariable("id") Integer id, @RequestParam("showState") Integer showState) {

        UserTalk userTalk = adminService.modifyPrivateShow(id, showState);
        return ResultVOUtil.success(userTalk);

    }

    /**
     * 互动
     */
    @GetMapping("/public/{id}")
    public ResultVO findPublicMsg(@PathVariable("id") Integer id,
                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "100") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        Map<String, Object> publicMsg = adminService.findPublicMsg(id, pageRequest);
        return ResultVOUtil.success(publicMsg);
    }

    /**
     * 显示 隐藏 互动
     */
    @PostMapping("/public/{id}")
    public ResultVO modifyPublicShow(@PathVariable("id") Integer id, @RequestParam("showState") Integer showState) {

        PublishTalk userTalk = adminService.modifyPublicShow(id, showState);
        return ResultVOUtil.success(userTalk);

    }


    /**
     * 获取宠物分类&品种
     *
     * @return
     */
    @GetMapping("/variety")
    public ResultVO petClassifyAndVariety() {

        List<PetVariety> petVarieties = petVarietyRepository.findAll();
        if (petVarieties.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        Map<Integer, List<PetVariety>> varietyMap = petVarieties.stream().collect(Collectors.groupingBy(variety -> variety.getClassifyId()));
        return ResultVOUtil.success(varietyMap);
    }

    /**
     * 获取宠物品种
     */
    public Map<Integer, PetVariety> petVariety() {

        List<PetVariety> petVarieties = petVarietyRepository.findAll();
        if (petVarieties.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        Map<Integer, PetVariety> petVarietyMap = petVarieties.stream().collect(Collectors.toMap(PetVariety::getId, Function.identity()));
        return petVarietyMap;
    }


    /**
     * 获取企业认证
     *
     * @return
     */
    @GetMapping("/approve")
    public ResultVO approveList(@RequestParam(value = "approveState", defaultValue = "-1") Integer approveState,
                                @RequestParam(value = "number", defaultValue = "0") Integer number,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageRequest request = new PageRequest(number, size);
        Map<String, Object> approve = adminService.findApprove(approveState, request);
        return ResultVOUtil.success(approve);
    }


    /**
     * 审核认证
     *
     * @param id
     * @param approveType
     * @param msg
     * @return
     */
    @PostMapping("/approve/{id}")
    public ResultVO approveResult(@PathVariable("id") Integer id,
                                  @RequestParam("approveType") Integer approveType,
                                  @RequestParam(value = "msg", defaultValue = "") String msg) {

        boolean res = adminService.modifyApprove(id, approveType, msg);
        return ResultVOUtil.success(res);
    }

    /**
     * 浏览记录
     *
     * @param id
     * @param day
     * @return
     */
    @GetMapping("/view/{id}")
    public ResultVO findView(@PathVariable("id") Integer id,
                             @RequestParam(value = "day", defaultValue = "0") Integer day) {

        Map<String, Object> view = adminService.findView(id, day);
        return ResultVOUtil.success(view);
    }

    @Autowired
    private SearchServiceImpl searchService;

    @GetMapping("/search")
    public ResultVO search(@RequestParam("keywords") String keywords,
                           @RequestParam(value = "publishType", defaultValue = "0") Integer publishType) {

        if (publishType == 0) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR);
        }

        Map<String, Object> map = searchService.adminPetSearch(keywords, publishType);
        return ResultVOUtil.success(map);
    }


}
