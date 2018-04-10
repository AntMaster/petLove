package com.shumahe.pethome.Domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "membertagsmapping")
public class MemberTagsMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "memberid")
    private Integer memberId;


    @Column(name = "tagid")
    private Integer tagId;

}
