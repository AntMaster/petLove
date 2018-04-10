package com.shumahe.pethome.Service;

import com.shumahe.pethome.DTO.PublishDTO;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserDynamic;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface DynamicService {


    /**
     * 首页   关注，取关
     * @param openId
     * @param userDynamic
     * @return
     */
    boolean likePublish(String openId,UserDynamic userDynamic);



    /**
     * 首页   我的关注
     * @param openId
     * @param
     * @return
     */
    List<PublishDTO> MyLikes(String openId);




    /**
     * 我的转发;转发我的
     * @param openId
     * @param type
     * @return
     */
    List<Map<String,String>> findMyShare(String openId , int type);

    /**
     * 转发
     * @param openId
     * @param userDynamic
     * @return
     */
    boolean sharePublish(String openId, UserDynamic userDynamic);


    /**
     * 分享操作(主页)
     *
     *  PS:基于微信
     */



    /**
     * 关注列表(主页)
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
