package com.shumahe.pethome.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String openId;

    private String nickName;

    private String headImgUrl;

    private Integer approveType;

    private Integer approveState;

    private List<String> tagsName;

    private String tagName;

    private String mobile;

    private Integer publishCount;


    private Integer unFinishCount;

    private Integer privateMsgCount;


    private Integer publicMsgCount;


    private Integer likeCount;


    private Integer shareCount;


    public UserDTO() {

    }

    public UserDTO(String openId, String nickName, String headImgUrl) {

        this.openId = openId;
        this.nickName = nickName;
        this.headImgUrl = headImgUrl;
    }

}
