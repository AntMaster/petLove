package com.shumahe.pethome.Domain;

import com.shumahe.pethome.Enums.ApproveStateEnum;
import com.shumahe.pethome.Enums.ApproveTypeEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "userapprove")
@EntityListeners(AuditingEntityListener.class)
public class UserApprove {

    @Id
    @GeneratedValue
    private Integer id;


    @Column(name = "userid")
    private String userId;


    @Column(name = "approvetype")
    private Integer approveType = ApproveTypeEnum.ASSOCIATION.getCode();


    @Column(name = "organizationname")
    private String organizationName;


    @Column(name = "organizationimage")
    private String organizationImage;


    @Column(name = "dutyer")
    private String dutyer;


    @Column(name = "dutyerphone")
    private String dutyerPhone;


    @Column(name = "dutyerno")
    private String dutyerNo;


    @Column(name = "credentials")
    private String credentials;


    @Column(name = "approvestate")
    private Integer approveState = ApproveStateEnum.WAITING.getCode();


    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "createtime")
    private Date createTime;
}
