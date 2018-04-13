package com.shumahe.pethome.Service.Impl;

import com.shumahe.pethome.Config.ProjectUrlConfig;
import com.shumahe.pethome.Controller.WechatController;
import com.shumahe.pethome.DTO.PrivateMsgDTO;
import com.shumahe.pethome.DTO.PublicMsgDTO;
import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.DTO.UserApproveDTO;
import com.shumahe.pethome.Domain.*;
import com.shumahe.pethome.Enums.ApproveStateEnum;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.*;
import com.shumahe.pethome.Service.AdminService;
import com.shumahe.pethome.Service.BaseService.PublishBaseService;
import com.shumahe.pethome.Util.DateUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    PetPublishRepository petPublishRepository;

    @Autowired
    PublishBaseService publishBaseService;

    @Autowired
    UserDynamicRepository userDynamicRepository;

    @Autowired
    UserBasicRepository userBasicRepository;

    @Autowired
    UserTalkRepository userTalkRepository;

    @Autowired
    PublishTalkRepository publishTalkRepository;


    @Autowired
    UserApproveRepository userApproveRepository;

    @Autowired
    PublishViewRepository publishViewRepository;


    /**
     * 查询寻宠 、 寻主
     */
    @Override
    public Map<String, Object> findAll(Integer publishType, PageRequest pageRequest) {

        Page<PetPublish> pets = petPublishRepository.findByPublishTypeOrderByCreateTimeDesc(publishType, pageRequest);
        List<PetPublish> publishList = pets.getContent();
        if (publishList.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        //List<PublishDTO> publishDTOS = publishBaseService.findPetExtends(publishList);
        List<PublishDTO> publishDTOS = publishBaseService.findPetsDetail(publishList);

        Map<String, Object> res = new HashMap<>();
        res.put("total", pets.getTotalElements());
        res.put("pages", pets.getTotalPages());
        res.put("size", pets.getSize());
        res.put("page", pets.getNumber());
        res.put("data", publishDTOS);

        return res;
    }

    /**
     * 显示 隐藏
     */

    @Override
    public PetPublish modifyShowState(Integer id, Integer publishState) {
        PetPublish publish = petPublishRepository.findById(id);
        if (publish == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        
        publish.setPublishState(publishState);

        PetPublish save = petPublishRepository.save(publish);
        return save;
    }

    /**
     * 转发 || 关注
     */
    @Override
    public Map<String, Object> findDynamic(Integer id, Integer dynamicType, Integer day) {

        //发布信息
        PetPublish publish = petPublishRepository.findById(id);

        if (publish == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        //动态
        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        Date startTime = DateUtil.getStartTime(now.getTime());
        Date endTime = DateUtil.getEndTime(now.getTime());
        List<UserDynamic> dynamics = userDynamicRepository.findByPublishIdAndDynamicTypeAndCreateTimeBetweenOrderByCreateTimeDesc(id, dynamicType, startTime, endTime);


        Map<String, Object> resMap = new HashMap<>();
        resMap.put("createDate", publish.getCreateTime().toString());//发布时间

        if (dynamics.isEmpty()) {
            return resMap;
        }

        List<String> usersId = dynamics.stream().map(e -> e.getUserIdFrom()).distinct().collect(toList());
        List<UserBasic> users = userBasicRepository.findByOpenIdIn(usersId);


        List<Map<String, String>> collect = dynamics.stream().map(dynamic -> {

            Map<String, String> _temp = new HashMap<>();
            _temp.put("dynamicDate", dynamic.getCreateTime().toString().split(" ")[0]);
            _temp.put("dynamicTime", dynamic.getCreateTime().toString());

            users.stream().forEach(user -> {
                if (dynamic.getUserIdFrom().trim().equals(user.getOpenId().trim())) {
                    _temp.put("nickName", user.getNickName());
                    _temp.put("userImage", user.getHeadImgUrl());
                    _temp.put("openId", user.getOpenId());
                }
            });
            return _temp;

        }).collect(toList());

        resMap.put("dynamicData", collect);
        return resMap;
    }

    /**
     * 私信
     */
    @Override
    public Map<String, Object> findPrivateMsg(Integer id, PageRequest pageRequest) {

        PetPublish publish = petPublishRepository.findById(id);

        if (publish == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        Page<UserTalk> page = userTalkRepository.findByPublishIdOrderByIdDesc(id, pageRequest);

        List<UserTalk> talks = page.getContent();
        if (talks.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        List<String> usersId = talks.stream().map(e -> e.getUserIdFrom()).distinct().collect(toList());

        List<UserBasic> users = userBasicRepository.findByOpenIdIn(usersId);


        List<PrivateMsgDTO> msgDTOS = new ArrayList<>();

        talks.stream().forEach(msg -> {

            PrivateMsgDTO msgDTO = new PrivateMsgDTO();
            BeanUtils.copyProperties(msg, msgDTO);

            users.stream().forEach(user -> {
                if (msg.getUserIdFrom().trim().equals(user.getOpenId().trim())) {
                    msgDTO.setUserIdFromName(user.getNickName());
                    msgDTO.setUserIdFromPhoto(user.getHeadImgUrl());
                }
            });
            msgDTOS.add(msgDTO);
        });


        Map<String, Object> res = new HashMap<>();
        res.put("total", page.getTotalElements());
        res.put("pages", page.getTotalPages());
        res.put("size", page.getSize());
        res.put("page", page.getNumber());
        res.put("data", msgDTOS);

        return res;
    }


    /**
     * 显示 隐藏 私信
     */
    @Override
    public UserTalk modifyPrivateShow(Integer id, Integer showState) {
        UserTalk msg = userTalkRepository.findOne(id);
        if (msg == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }


        msg.setShowState(showState);

        UserTalk save = userTalkRepository.save(msg);
        return save;

    }

    /**
     * 互动
     */
    @Override
    public Map<String, Object> findPublicMsg(Integer id, PageRequest pageRequest) {


        PetPublish publish = petPublishRepository.findById(id);

        if (publish == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        Page<PublishTalk> page = publishTalkRepository.findByPublishIdOrderByReplyDateDesc(id, pageRequest);
        List<PublishTalk> talks = page.getContent();
        if (talks.isEmpty()) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        List<String> usersId = talks.stream().map(e -> e.getReplierFrom()).distinct().collect(toList());

        List<UserBasic> users = userBasicRepository.findByOpenIdIn(usersId);


        List<PublicMsgDTO> msgDTOS = new ArrayList<>();

        talks.stream().forEach(msg -> {

            PublicMsgDTO msgDTO = new PublicMsgDTO();
            BeanUtils.copyProperties(msg, msgDTO);

            users.stream().forEach(user -> {
                if (msg.getReplierFrom().trim().equals(user.getOpenId().trim())) {
                    msgDTO.setReplierFromName(user.getNickName());
                    msgDTO.setReplierFromPhoto(user.getHeadImgUrl());
                }
            });
            msgDTOS.add(msgDTO);
        });

        Map<String, Object> res = new HashMap<>();
        res.put("total", page.getTotalElements());
        res.put("pages", page.getTotalPages());
        res.put("size", page.getSize());
        res.put("page", page.getNumber());
        res.put("data", msgDTOS);
        return res;
    }


    /**
     * 显示 隐藏 互动
     */
    @Override
    public PublishTalk modifyPublicShow(Integer id, Integer showState) {

        PublishTalk msg = publishTalkRepository.findOne(id);
        if (msg == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }
        msg.setShowState(showState);
        PublishTalk save = publishTalkRepository.save(msg);
        return save;
    }

    /**
     * 企业认证
     *
     * @param approveState
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> findApprove(Integer approveState, PageRequest request) {


        Page<UserApprove> all;
        if (approveState == -1) {
            all = userApproveRepository.findAllByOrderByCreateTimeDesc(request);
        } else {
            all = userApproveRepository.findByApproveStateOrderByCreateTimeDesc(approveState, request);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("last", all.isLast());
        res.put("totalPages", all.getTotalPages());
        res.put("totalElements", all.getTotalElements());
        res.put("number", all.getNumber());
        res.put("size", all.getSize());
        res.put("first", all.isFirst());
        res.put("numberOfElements", all.getNumberOfElements());
        List<UserApprove> approves = all.getContent();
        if (approves.isEmpty()) {
            return res;
        }

        List<String> userId = approves.stream().map(UserApprove::getUserId).distinct().collect(toList());
        List<UserBasic> users = userBasicRepository.findByOpenIdIn(userId);
        Map<String, UserBasic> userMap = users.stream().collect(Collectors.toMap(e -> e.getOpenId().trim(), Function.identity()));

        List<UserApproveDTO> userApprove = approves.stream().map(e -> {

            UserApproveDTO userApproveDTO = new UserApproveDTO();
            BeanUtils.copyProperties(e, userApproveDTO);
            userApproveDTO.setUserImage(userMap.get(e.getUserId()).getHeadImgUrl());
            return userApproveDTO;

        }).collect(toList());

        res.put("content", userApprove);
        return res;
    }

    /**
     * 浏览记录
     *
     * @param id
     * @return
     */
      /**
      * @Description:
      * @Param:  
      * @return:  
      * @Author: Mr.Wang 
      * @Date: 2018/4/12 
      */

    @Override
    public Map<String, Object> findView(Integer id, Integer day) {

        //当前发布
        PetPublish pet = petPublishRepository.findById(id);


        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);

        //当天的浏览量
        Date startTime = DateUtil.getStartTime(now.getTime());
        Date endTime = DateUtil.getEndTime(now.getTime());
        List<PublishView> viewers = publishViewRepository.findByPublishIdAndViewTimeBetweenOrderByViewTimeDesc(id, startTime, endTime);


        Map<String, Object> resMap = new HashMap<>();
        resMap.put("publishDate", pet.getCreateTime().toString());
        if (viewers.isEmpty()) {
            return resMap;
        }

        List<String> userIds = viewers.stream().map(PublishView::getViewer).distinct().collect(toList());
        List<UserBasic> users = userBasicRepository.findByOpenIdIn(userIds);
        Map<String, UserBasic> usersMap = users.stream().collect(Collectors.toMap(e -> e.getOpenId().trim(), Function.identity()));


        resMap.put("data", viewers.stream().map(e -> {
            UserBasic curUser = usersMap.get(e.getViewer());
            Map<String, String> _temp = new HashMap<>();
            _temp.put("viewerId", curUser.getOpenId());
            _temp.put("viewerName", curUser.getNickName());
            _temp.put("viewerImage", curUser.getHeadImgUrl());
            _temp.put("viewTime", e.getViewTime().toString());
            _temp.put("publishTime", pet.getCreateTime().toString());
            return _temp;
        }).collect(toList()));

        return resMap;
    }

    @Autowired
    WechatController wechatController;

    /**
     * 认证审核
     *
     * @param id
     * @param approveType
     * @param msg
     * @return
     */
    @Transactional
    @Override
    public boolean modifyApprove(Integer id, Integer approveType, String msg) {

        UserApprove approve = userApproveRepository.findOne(id);
        if (approve == null)
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);

        approve.setApproveState(approveType);
        approve.setDescription(msg);
        userApproveRepository.save(approve);

        UserBasic userBasic = userBasicRepository.findByOpenId(approve.getUserId());
        if (userBasic == null)
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);

        userBasic.setApproveState(approveType);
        userBasicRepository.save(userBasic);

        try {
            wechatController.templateMsgPush(approve.getUserId(), approve);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    WxMpService wxMpService;


/*
    private String templateMsgPush(String openId,
                       UserApprove approve) throws WxErrorException {
        WxMpConfigStorage configStorage = wxMpService.getWxMpConfigStorage();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        String keyword2Msg = "审核通过!";
        String reMarkMsg = "恭喜您！（" + approve.getOrganizationName() + "）已通过认证，去解锁更多动物信息!";

        if (approve.getApproveState() == ApproveStateEnum.FAILURE.getCode()) {
            keyword2Msg = "审核失败!";
            reMarkMsg = "很遗憾！（" + approve.getOrganizationName() + "）未能通过认证，" + approve.getDescription() + "!";
        }

        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(configStorage.getTemplateId())
                .url(projectUrlConfig.getWechatMpAuthorize() + "/pethome/mine.html?openid=" + openId)
                .build();

        templateMessage
                .addData(new WxMpTemplateData("first", "您好，您提交的实名审核已经完成！", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword1", "实名认证审核", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword2", keyword2Msg, "#FF00FF"))
                .addData(new WxMpTemplateData("keyword3", dateFormat.format(new Date()), "#FF00FF"))
                .addData(new WxMpTemplateData("remark", reMarkMsg, "#FF00FF"));
        String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        return msgId;

        }*/
}
