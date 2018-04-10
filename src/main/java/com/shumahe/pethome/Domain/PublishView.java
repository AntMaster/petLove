package com.shumahe.pethome.Domain;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "publishview")
public class PublishView {

    /**
     * id
     */
    @Id
    @GeneratedValue
    private Integer id;


    /**
     * 浏览者
     */
    private String viewer;


    /**
     * 浏览时间
     */
    private Date viewTime;


    /**
     * 浏览主题
     */
    private Integer publishId;


    /**
     * 发布人ID
     */
    private String publisherId;


}
