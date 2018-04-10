package com.shumahe.pethome.Service;

import com.shumahe.pethome.DTO.PrivateMsgDTO;
import com.shumahe.pethome.DTO.PublicMsgDTO;
import com.shumahe.pethome.DTO.UserDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Form.ReplyPrivateForm;
import com.shumahe.pethome.Form.ReplyPublishForm;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface MessageService {


    /**
     * 查询 我的私信
     *
     * @param openId
     * @param pageRequest
     * @return
     */
    //List<List<LinkedHashMap<String, String>>> findMyPrivateTalk(String openId, PageRequest pageRequest);


    /**
     * 查询 我的互动
     *
     * @param openId
     * @param pageRequest
     * @return
     */
    //List<List<Map<String, String>>> findMyPublicTalk(String openId, PageRequest pageRequest);


    /**
     * 查询 主题互动
     *
     * @param pet
     * @return
     */
    List<List<PublicMsgDTO>> petPublicTalks(PetPublish pet);


    /**
     * 查询  主题私信
     *
     * @param pet
     * @return
     */
    List<List<PrivateMsgDTO>> petPrivateTalks(PetPublish pet, String openId);


    /**
     * 回复私信
     *
     * @param replyPrivateForm
     */
    PrivateMsgDTO replyPrivate(ReplyPrivateForm replyPrivateForm, PetPublish pet);


    /**
     * 回复互动
     *
     * @param replyPublishForm
     */
    PublicMsgDTO replyPublic(ReplyPublishForm replyPublishForm, PetPublish pet);



}
