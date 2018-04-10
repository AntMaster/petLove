package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum ContraceptionStateEnum {

    TRUE(1, "已节育"),
    FALSE(0, "未节育");

    private int code;

    private String message;

    ContraceptionStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
