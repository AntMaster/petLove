package com.shumahe.pethome.Domain;

import com.shumahe.pethome.Enums.ShowStateEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "userpetalbum")
public class UserPetAlbum {

    @Id
    @GeneratedValue()
    private Integer id;


    /**
     * 宠物ID
     */
    @Column(name = "petid")
    private Integer petId;

    /**
     * 相册名称
     */
    @Column(name = "name")
    private String name;


    /**
     * 相册封面
     */
    @Column(name = "coverpath")
    private String coverPath;


    /**
     * 相册描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 显示状态
     */
    @Column(name = "show")
    private Integer show  = ShowStateEnum.SHOW.getCode();


    @CreatedDate
    @Column(name = "createtime")
    private Date createTime;


}
