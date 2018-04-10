package com.shumahe.pethome.Service.Impl;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserDynamic;
import com.shumahe.pethome.Enums.DynamicTypeEnum;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.Repository.UserDynamicRepository;
import com.shumahe.pethome.Service.BaseService.DynamicBaseService;
import com.shumahe.pethome.Service.BaseService.PublishBaseService;
import com.shumahe.pethome.Service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DynamicServiceImpl implements DynamicService {

    @Autowired
    UserDynamicRepository userDynamicRepository;

    @Autowired
    UserBasicRepository userBasicRepository;

    @Autowired
    PetPublishRepository petPublishRepository;


    @Autowired
    DynamicBaseService dynamicBaseService;

    @Autowired
    PublishBaseService publishBaseService;

    /**
     * 首页   关注，取关
     *
     * @param openId
     * @param userDynamic
     * @return
     */
    @Override
    public boolean likePublish(String openId, UserDynamic userDynamic) {


        if (userDynamic.getPublishId() == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "发布ID必填");
        }
        if (userDynamic.getDynamicType() == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "互动类型必填");
        }

        PetPublish pet = petPublishRepository.findById(userDynamic.getPublishId());
        if (pet == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "发布ID不存在");
        }


        List<UserDynamic> dynamic = userDynamicRepository.findByUserIdFromAndPublishId(openId, userDynamic.getPublishId());
        if (!dynamic.isEmpty()) {

            dynamic.forEach(e -> {

                if (e.getDynamicType() == DynamicTypeEnum.LIKE.getCode()) {
                    e.setDynamicType(DynamicTypeEnum.CANCEL.getCode());
                } else if (e.getDynamicType() == DynamicTypeEnum.CANCEL.getCode()) {
                    e.setDynamicType(DynamicTypeEnum.LIKE.getCode());
                }
            });
            userDynamicRepository.save(dynamic);

        } else {

            //首次关注
            userDynamic.setUserIdFrom(openId);
            userDynamic.setUserIdArrive(pet.getPublisherId());
            userDynamicRepository.save(userDynamic);
        }
        return true;
    }

    /**
     * 我的关注
     *
     * @param openId
     * @return
     */
    @Override
    public List<PublishDTO> MyLikes(String openId) {

        /**
         * 1.查询dynamic表基本信息
         *
         * 2.查询member得到用户信息
         *
         * 3.查询publish表得到发布信息
         *
         */
        //step 1
        List<UserDynamic> userDynamics = userDynamicRepository.findByUserIdFromAndDynamicTypeOrderByCreateTimeDesc(openId, DynamicTypeEnum.LIKE.getCode());

        //data empty
        if (userDynamics.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        List<Integer> publishIds = userDynamics.stream().map(e -> e.getPublishId()).distinct().collect(Collectors.toList());


        List<PetPublish> pets = petPublishRepository.findByIdInOrderByCreateTimeDesc(publishIds);
        /**
         * BaseService查关联信息
         */
        List<PublishDTO> list = publishBaseService.findPetExtends(pets);
        list.forEach(e -> e.setLikeState(true));

        return list;

    }


    /**
     * 转发列表(我的转发+转发我的)
     *
     * @param openId
     * @param type
     * @return
     */
    @Override
    public List<Map<String, String>> findMyShare(String openId, int type) {

        /**
         * 1.查询dynamic表基本信息
         *
         * 2.查询member得到用户信息
         *
         * 3.查询publish表得到发布信息
         *
         */

        //step 1
        List<UserDynamic> userDynamics = new ArrayList<>();

        if (type == 1) {//我的转发

            userDynamics = userDynamicRepository.findByUserIdFromAndDynamicTypeOrderByCreateTimeDesc(openId, DynamicTypeEnum.SHARE.getCode());

        } else if (type == 2) {//转发我的

            userDynamics = userDynamicRepository.findByUserIdArriveAndDynamicTypeOrderByCreateTimeDesc(openId, DynamicTypeEnum.SHARE.getCode());
        }

        //data empty
        if (userDynamics.size() == 0) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        /**
         * baseDynamicService 根据互动类型返回最终结果
         */
        List<Map<String, String>> shareList = dynamicBaseService.findLikeOrShareList(userDynamics, type);


        return shareList;

    }

    /**
     * 转发
     *
     * @param openId
     * @param userDynamic
     * @return
     */
    @Override
    public boolean sharePublish(String openId, UserDynamic userDynamic) {

        if (userDynamic.getPublishId() == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "发布ID必填");
        }
        if (userDynamic.getDynamicType() == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "互动类型必填");
        }

        PetPublish pet = petPublishRepository.findById(userDynamic.getPublishId());
        if (pet == null) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "发布ID不存在");
        }


        userDynamic.setUserIdFrom(openId);
        userDynamic.setUserIdArrive(pet.getPublisherId());
        userDynamicRepository.save(userDynamic);

        return true;
    }


}
