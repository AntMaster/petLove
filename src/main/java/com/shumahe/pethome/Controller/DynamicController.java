package com.shumahe.pethome.Controller;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserDynamic;
import com.shumahe.pethome.Enums.DynamicTypeEnum;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;

import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.UserDynamicRepository;
import com.shumahe.pethome.Service.BaseService.DynamicBaseService;
import com.shumahe.pethome.Service.DynamicService;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/dynamic")
public class DynamicController {


    @Autowired
    DynamicService dynamicService;

    @Autowired
    DynamicBaseService dynamicBaseService;

    @Autowired
    PetPublishRepository petPublishRepository;

    @Autowired
    UserDynamicRepository userDynamicRepository;

    /**
     * 我的关注
     *
     * @param openId
     * @return
     */
    @GetMapping("/like/{openId}")
    public ResultVO mylikes(@PathVariable("openId") String openId) {

        List<PublishDTO> publishDTOS = dynamicService.MyLikes(openId);

        return ResultVOUtil.success(publishDTOS);
    }


    /**
     * 关注  取关 操作
     *
     * @param openId
     * @return
     */
    @PutMapping("/like/{openId}")
    public ResultVO likePublish(@PathVariable("openId") String openId, @RequestBody UserDynamic userDynamic) {

        boolean state = dynamicService.likePublish(openId, userDynamic);
        return ResultVOUtil.success(state);
    }

    /**
     *  分享
     * @param openId
     * @param userDynamic
     * @return
     */
    @PutMapping("/share/{openId}")
    public ResultVO sharePublish(@PathVariable("openId") String openId, @RequestBody UserDynamic userDynamic) {

        boolean state = dynamicService.sharePublish(openId, userDynamic);
        return ResultVOUtil.success(state);
    }

    /**
     * 转发列表(我的关注 + 关注我的)
     *
     * @param openId
     * @param type
     * @return
     */
    @GetMapping("/share/list")
    public ResultVO<List<Map<String, String>>> shareList(@RequestParam("openid") String openId, @RequestParam("type") int type) {


        if (type != 1 && type != 2) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "参数不正确,type=1为我的转发,type=2为转发我的");
        }

        List<Map<String, String>> likeResult = dynamicService.findMyShare(openId, type);

        return ResultVOUtil.success(likeResult);

    }


    /**
     * 分享操作(主页)
     *
     *  PS:基于微信
     */


    /**
     * 关注列表(我的)
     */


    /**
     * 被关注列表(我的)
     */

    /**
     * 分享列表(我的)
     */


    /**
     * 被分享列表(我的)
     */



}
