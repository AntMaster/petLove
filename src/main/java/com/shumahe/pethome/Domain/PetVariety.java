package com.shumahe.pethome.Domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "petvariety")
public class PetVariety {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 宠物种类（猫狗兔）
     */
    @Column(name = "classifyid")
    private Integer classifyId;


    /**
     * 种类类别名称（哈士奇 金毛）
     */
    @Column(name = "name")
    private String name;

}
