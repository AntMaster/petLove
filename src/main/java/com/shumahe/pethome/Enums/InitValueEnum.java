package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum InitValueEnum {

    INTEGER_EMPTY(-1, "整形为空"),
    STRING_EMPTY (-1, "字符串为空");

    private int code;

    private String message;

    InitValueEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
