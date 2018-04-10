package com.shumahe.pethome.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserBasicVO {

    /**
     * 用户ID
     */
    @JsonProperty(value = "memberId")
    private int id;

    private String nickName;

    private String headImageUrl;

    private String openId;

}
