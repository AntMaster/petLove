package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum RewardTypeEnum {

    EXPENSE(1, "支付"),
    INCOME(0, "收入");

    private int code;

    private String message;

    RewardTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
