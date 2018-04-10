package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum ApproveStateEnum {

    FAILURE(0, "认证失败"),
    SUCCESS(1, "认证成功"),
    WAITING(2, "待认证");

    private int code;

    private String message;

    ApproveStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
