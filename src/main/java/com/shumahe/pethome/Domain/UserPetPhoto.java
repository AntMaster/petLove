package com.shumahe.pethome.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shumahe.pethome.Enums.BooleanEnum;
import com.shumahe.pethome.Enums.ShowStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "userpetphoto")
@EntityListeners(AuditingEntityListener.class)
public class UserPetPhoto {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "petid")
    private Integer petId;


    @Column(name = "albumid")
    private Integer albumId;


    @Column(name = "name")
    private String name;


    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @Column(name = "show")
    private Integer show = ShowStateEnum.SHOW.getCode();


    @CreatedDate
    @Column(name = "createtime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
