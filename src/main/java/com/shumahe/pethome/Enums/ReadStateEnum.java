package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum ReadStateEnum {

    NOT_READ(0, "未读"),
    READ(1, "已读");


    private int code;

    private String message;


    ReadStateEnum(int code, String massage) {
        this.code = code;
        this.message = massage;

    }
}
