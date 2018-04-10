package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum RewardStateEnum {

    WAIT(1, "等待支付"),
    SUCCESS(2, "支付成功"),
    CANCEL(3, "支付取消"),
    EXCEPTION(4, "支付异常");

    private int code;

    private String message;

    RewardStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
