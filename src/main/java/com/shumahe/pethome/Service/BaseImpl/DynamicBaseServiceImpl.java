package com.shumahe.pethome.Service.BaseImpl;

import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Domain.UserDynamic;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.Service.BaseService.DynamicBaseService;
import com.shumahe.pethome.Util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DynamicBaseServiceImpl implements DynamicBaseService {


    @Autowired
    UserBasicRepository userBasicRepository;

    @Autowired
    PetPublishRepository petPublishRepository;

    /**
     * @param userDynamics
     * @param type         type = 1 我的（关注/转发）  type = 2  （关注/转发）我的
     * @return
     */
    @Override
    public List<Map<String, String>> findLikeOrShareList(List<UserDynamic> userDynamics, int type) {


        if (userDynamics.size() == 0) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(), "findLikeAndShareList方法参数为空");
        }

        //人员信息
        List<String> userFrom = userDynamics.stream().map(UserDynamic::getUserIdFrom).collect(Collectors.toList());
        List<String> userAccept = userDynamics.stream().map(UserDynamic::getUserIdArrive).collect(Collectors.toList());
        userFrom.addAll(userAccept);
        List<UserBasic> users = userBasicRepository.findByOpenIdIn(userFrom);
        Map<String, UserBasic> usersMap = users.stream().collect(Collectors.toMap(e -> e.getOpenId().trim(), Function.identity()));

        //发布信息
        List<Integer> themeId = userDynamics.stream().map(UserDynamic::getPublishId).distinct().collect(Collectors.toList());
        List<PetPublish> themes = petPublishRepository.findByIdInOrderByCreateTimeDesc(themeId);
        Map<Integer, PetPublish> themesMap = themes.stream().collect(Collectors.toMap(PetPublish::getId, Function.identity()));

        List<Map<String, String>> result = new ArrayList<>();

        userDynamics.forEach(e -> {

            UserBasic curUserFrom = usersMap.get(e.getUserIdFrom());
            UserBasic curUserAccept = usersMap.get(e.getUserIdArrive());
            PetPublish curTheme = themesMap.get(e.getPublishId());

            Map<String, String> _temp = new HashMap<>();
            _temp.put("acceptUserId", curUserAccept.getOpenId());
            _temp.put("acceptUserName", curUserAccept.getNickName());
            _temp.put("acceptUserImage", curUserAccept.getHeadImgUrl());


            _temp.put("userFromId", curUserFrom.getOpenId());
            _temp.put("userFromName", curUserFrom.getNickName());
            _temp.put("userFromImage", curUserFrom.getHeadImgUrl());
            _temp.put("approveState", String.valueOf(curUserFrom.getApproveState()));
            _temp.put("approveType", String.valueOf(curUserFrom.getApproveType()));


            _temp.put("lostTime", curTheme.getLostTime().toString().split(" ")[0]);
            _temp.put("petName", curTheme.getPetName());
            _temp.put("petImage", curTheme.getPetImage());
            _temp.put("publishType", curTheme.getPublishType().toString());

            _temp.put("attentionTime", e.getCreateTime().toString());

            result.add(_temp);
        });

        return  result;

        /**
         * 本人信息
         */
       /* String myselfOpenId = null;
        if (type == 1) {

            myselfOpenId = userDynamics.get(0).getUserIdFrom();

        } else if (type == 2) {
            myselfOpenId = userDynamics.get(0).getUserIdArrive();

        }

        UserBasic myselfInfo = userBasicRepository.findByOpenId(myselfOpenId);*/
        /**
         *
         * 1.查询（关注我的，转发我的，我关注的，关注我的）发布信息
         *
         * 2.查询（关注我的，转发我的，我关注的，关注我的）人员信息
         *
         * 3.整理 动态信息 + 发布信息 + 人员信息
         *
         *
         */

       /* List<String> friendsId = new ArrayList<>();
        List<Integer> publishIds = new ArrayList<>();
        userDynamics.forEach(dynamic -> {

            if (type == 1) {

                friendsId.add(dynamic.getUserIdArrive());//其他人OpenID
                dynamic.setUserIdFrom(dynamic.getUserIdArrive());//将自己openID全部转化为其他人-->后面整理数据不用再判断,随便取

            } else if (type == 2) {

                friendsId.add(dynamic.getUserIdFrom());//其他人OpenID
                dynamic.setUserIdArrive(dynamic.getUserIdFrom());//将自己openID全部转化为其他人

            }

            publishIds.add(dynamic.getPublishId());

        });

        //其他人openId去重
        //List<String> finalFriendsId = CollectionUtil.removeRepeatStringItem(friendsId);
        List<String> finalFriendsId = friendsId.stream().distinct().collect(Collectors.toList());

        *//**
         * step 1
         *//*
        List<UserBasic> friends = userBasicRepository.findByOpenIdIn(finalFriendsId);

        *//**
         * step 2
         *//*
        List<PetPublish> publishes = petPublishRepository.findByIdIn(publishIds);


        *//**
         * step 3
         *//*
        List<Map<String, String>> resultSorted = new ArrayList<>();

        userDynamics.forEach(dynamic -> {

            Map<String, String> _tempMap = new HashMap<>();

            //userInfo
            friends.forEach(friend -> {

                if (dynamic.getUserIdArrive().trim().equals(friend.getOpenId().trim())) {//getUserIdArrive()与getUserIdFrom()都可以,service层已转化

                    _tempMap.put("publisherName", friend.getNickName());
                    _tempMap.put("publisherId", friend.getOpenId());
                    _tempMap.put("publisherHeadImage", friend.getHeadImgUrl());
                    _tempMap.put("likeType", String.valueOf(type));
                }
            });

            //publishInfo
            publishes.forEach(publish -> {
                if (dynamic.getPublishId() == publish.getId()) {

                    _tempMap.put("petImage", publish.getPetImage());
                    _tempMap.put("petName", publish.getPetName());
                    _tempMap.put("publishType", String.valueOf(publish.getPublishType()));
                    _tempMap.put("petClassify", String.valueOf(publish.getClassifyId()));
                    _tempMap.put("lostTime", publish.getLostTime().toString().split(" ")[0]);
                }

            });

            _tempMap.put("myName", myselfInfo.getNickName());
            _tempMap.put("myHeadImage", myselfInfo.getHeadImgUrl());
            _tempMap.put("attentionTime", dynamic.getCreateTime().toString());

            resultSorted.add(_tempMap);

        });

        return resultSorted;*/
    }
}


