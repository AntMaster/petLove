package com.shumahe.pethome.Domain;

import com.shumahe.pethome.Enums.PetFindStateEnum;
import com.shumahe.pethome.Enums.PublishStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "petpublish")
@EntityListeners(AuditingEntityListener.class)
public class PetPublish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 发布类型
     */
    private Integer publishType;

    /**
     * 宠物类别
     */
    private Integer classifyId;

    /**
     * 宠物品种
     */
    private Integer varietyId;

    /**
     * 宠物昵称
     */
    private String petName;

    /**
     * 宠物性别
     */
    private Integer petSex;

    /**
     * 宠物描述
     */
    private String petDescription;

    /**
     * 宠物图片
     */
    private String petImage;


    /**
     * 丢失时间/发现时间
     */
    private Date lostTime;

    /**
     * 丢失地点/发现地点
     */
    private String lostLocation;

    /**
     * 经度
     */
    private Float latitude;



    /**
     * 纬度
     */
    private Float longitude;

    /**
     * 主人姓名/发现人
     */
    private String ownerName;


    /**
     * 联系方式
     */
    private String ownerContact;


    /**
     * 酬金数额
     */
    private BigDecimal reward;

    /**
     * 酬金有效开始时间
     */
    private Date rewardBegintime;

    /**
     * 酬金有效结束时间
     */
    private Date rewardEndtime;


    /**
     * 宠物寻找状态
     */
    private Integer findState = PetFindStateEnum.NOT_FOUND.getCode();

    /**
     * 浏览量
     */
    private Integer viewCount = 0;

    /**
     * 转发量
     */
    private Integer shareCount = 0;

    /**
     * 关注量
     */
    //private Integer likeCount = 0;

    /**
     * 发布状态
     */
    private Integer publishState = PublishStateEnum.SHOW.getCode();


    /**
     * 创建时间
     */
    @CreatedDate
    private Date createTime;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    private Date updateTime;
}
