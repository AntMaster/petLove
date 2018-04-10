package com.shumahe.pethome.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shumahe.pethome.Enums.ContraceptionStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "userpet")
@EntityListeners(AuditingEntityListener.class)
public class UserPet {

    @Id
    @GeneratedValue
    private Integer id;


    /**
     * 用户ID
     */
    @Column(name = "userid")
    private String userId;


    /**
     * 头像
     */
    @Column(name = "headimgurl")
    private String headImgUrl;


    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickName;


    /**
     * 分类
     */
    @Column(name = "classifyid")
    private Integer classifyId;


    /**
     * 品种
     */
    @Column(name = "varietyid")
    private Integer varietyId;


    /**
     * 性别
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 生日
     */
    @Column(name = "birthday")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 绝育状态
     */
    @Column(name = "contraception")
    private Integer contraception = ContraceptionStateEnum.FALSE.getCode();

    /**
     * 宠物描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 芯片号
     */
    @Column(name = "chipno")
    private String chipNo;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "createtime")
    private Date createTime;


}
