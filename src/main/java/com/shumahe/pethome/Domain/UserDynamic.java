package com.shumahe.pethome.Domain;

import com.shumahe.pethome.Enums.ReadStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "userdynamic")
@EntityListeners(AuditingEntityListener.class)
public class UserDynamic {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 动态操作人
     */
    @Column(name = "useridfrom")
    private String userIdFrom;

    /**
     * 动态类型
     */
    @Column(name = "dynamictype")
    private Integer dynamicType;

    /**
     * 发布ID
     */
    @Column(name = "publishid")
    private Integer publishId;

    /**
     * 发布人ID
     */
    @Column(name = "useridarrive")
    private String userIdArrive;


    /**
     * 阅读状态
     */
    @Column(name = "readstate")
    private Integer readState = ReadStateEnum.NOT_READ.getCode();


    /**
     * 互动提交时间
     */
    @CreatedDate
    @Column(name = "createtime")
    private Date createTime;


    /**
     * 最后一次互动时间
     */
    @LastModifiedDate
    @Column(name = "updatetime")
    private Date updateTime;


}
