package com.shumahe.pethome.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PublishTalkingVO {

    /**
     * 评论ID
     */
    @JsonProperty(value = "talkingId")
    private int id;

    /**
     * 发布ID
     */
    private int publishId;

    /**
     * 评论数量
     */
    private int commentCount ;


    /**
     * 留言详情
     */
   // private List<> commentCount ;
}
