package com.shumahe.pethome.Domain;

import com.shumahe.pethome.Enums.ReadStateEnum;
import com.shumahe.pethome.Enums.ShowStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "usertalk")
@EntityListeners(AuditingEntityListener.class)
public class UserTalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 主题ID
     */
    @Column(name = "publishid")
    private Integer publishId;


    /**
     * 主题发布人ID
     */
    @Column(name = "publisherid")
    private String publisherId;



    /**
     * 互动ID
     */
    @Column(name = "talkid")
    private Integer talkId;


    /**
     * 私信发起者
     */
    @Column(name = "useridfrom")
    private String userIdFrom;


    /**
     * 私信接收者
     */
    @Column(name = "useridaccept")
    private String userIdAccept;


    /**
     * 私信内容
     */
    @Column(name = "content")
    private String content;


    /**
     * 私信时间
     */
    @CreatedDate
    @Column(name = "talktime")
    private Date talkTime;



    /**
     * 显示状态
     */
    @Column(name = "showstate")
    private Integer showState = ShowStateEnum.SHOW.getCode();


    /**
     * 阅读状态
     */
    @Column(name = "readstate")
    private int readState = ReadStateEnum.NOT_READ.getCode();


    /**
     * 最后修改时间
     */
    @Column(name = "lastmodify")
    private Date lastModify;

}
