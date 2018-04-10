package com.shumahe.pethome.Domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class UserReward {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;

    private Integer publishId;

    private Integer rewordType;

    private Integer rewordState;

    private BigDecimal amount;

    private String description;

    private String paymentAccount;

    private String acceptAccount;

}
