package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum SexEnum {

    MALE(0, "男"),
    FEMALE(1, "女"),
    UNKNOWN(2, "未知");

    private int code;

    private String message;


    SexEnum(int code, String massage) {
        this.code = code;
        this.message = massage;

    }

}
