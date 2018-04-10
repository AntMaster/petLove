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
@Table(name = "publishtalk")
@EntityListeners(AuditingEntityListener.class)
public class PublishTalk {

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
     * 互动发送人
     */
    @Column(name = "replierfrom")
    private String replierFrom;

    /**
     * 互动接收人
     */
    @Column(name = "replieraccept")
    private String replierAccept;

    /**
     * 互动内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 互动时间
     */
    @CreatedDate
    @Column(name = "replydate")
    private Date replyDate;

    /**
     * 互动阅读状态
     */
    @Column(name = "readstate")
    private Integer readState = ReadStateEnum.NOT_READ.getCode();


    /**
     * 互动有效状态
     */
    @Column(name = "showstate")
    private Integer showState = ShowStateEnum.SHOW.getCode();


    @Column(name = "lastmodify")
    private Date lastModify;

}
