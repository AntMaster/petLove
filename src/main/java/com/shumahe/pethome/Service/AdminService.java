package com.shumahe.pethome.Service;

import com.shumahe.pethome.DTO.PrivateMsgDTO;
import com.shumahe.pethome.DTO.PublicMsgDTO;
import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.PublishTalk;
import com.shumahe.pethome.Domain.UserApprove;
import com.shumahe.pethome.Domain.UserTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface AdminService {
    /**
     * 查询寻宠 、 寻主
     */
    Map<String,Object> findAll(Integer publishType, PageRequest pageRequest);

    /**
     * 显示 隐藏
     */
    PetPublish modifyShowState(Integer id, Integer publishState);

    
    /**
     * 转发 关注
     */
    Map<String, Object> findDynamic(Integer id, Integer dynamicType, Integer day);


    /**
     * 私信
     */
    Map<String, Object> findPrivateMsg(Integer id, PageRequest pageRequest);


    /**
     * 显示 隐藏 私信
     */
    UserTalk modifyPrivateShow(Integer id, Integer showState);

    /**
     * 互动
     */
    Map<String, Object> findPublicMsg(Integer id, PageRequest pageRequest);


    /**
     * 显示 隐藏 互动
     */
    PublishTalk modifyPublicShow(Integer id, Integer showState);

    /**
     * 认证
     *
     */
    Map<String,Object> findApprove(Integer approveState, PageRequest request);

    /**
     * 浏览记录
     * @param id
     * @return
     */
    Map<String,Object> findView(Integer id,Integer day);

    /**
     * 认证审核
     * @param
     */
    boolean modifyApprove(Integer id, Integer approveType , String msg );
}
