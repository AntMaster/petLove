package com.shumahe.pethome.Domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "membertags")
public class MemberTags {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;


}
