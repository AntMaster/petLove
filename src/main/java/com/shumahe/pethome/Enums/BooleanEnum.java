package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum BooleanEnum {

    TRUE(1, "是"),
    FALSE(0, "否");

    private int code;

    private String message;

    BooleanEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
