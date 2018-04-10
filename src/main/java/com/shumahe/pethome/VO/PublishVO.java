package com.shumahe.pethome.VO;

import lombok.Data;

import java.util.Date;

@Data
public class PublishVO {


    private int id;


    private String publisherId;


    private int publishType;


    private String petImage;


    private int classifyId;


    private String petName;


    private Date lostTime;


    private String lostLocation;


    private Float latitude;


    private Float longitude;


    private String ownerName;


    private String ownerContact;


    /**
     * 只有评论条数
     */
    private PublishTalkingVO commentVO;


    private UserBasicVO userBasicVO;



}
