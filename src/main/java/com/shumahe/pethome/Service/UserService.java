package com.shumahe.pethome.Service;

import com.shumahe.pethome.DTO.UserDTO;
import com.shumahe.pethome.Domain.UserApprove;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Form.UserApproveForm;
import com.shumahe.pethome.VO.ResultVO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {



    UserDTO findMyInfo(String openId);

    /**
     * 企业认证
     * @param userApproveForm
     * @return
     */
    UserApprove saveOrganization(UserApproveForm userApproveForm, UserBasic userBasic);

    /**
     * 查询 我的私信
     *
     * @param openId
     * @param pageRequest
     * @return
     */
    List<Map<String, Object>> findMyPrivateTalk(String openId, PageRequest pageRequest);


    /**
     * 我的互动
     *
     * @param openId
     * @param pageRequest
     * @return
     */
    List<Map<String, Object>> findMyPublicTalk(String openId, PageRequest pageRequest);

    /**
     * 我的动态
     * @param openid
     * @param type
     * @return
     */
    List<Map<String,String>> findMyDynamic(String openid, Integer type);


    /**
     * 查看当前用户是否有未读消息
     */
    boolean  isShowMsgPoint(String openid);


}
