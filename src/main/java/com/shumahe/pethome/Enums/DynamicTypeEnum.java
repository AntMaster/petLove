package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum DynamicTypeEnum {


    LIKE(1, "关注"),
    SHARE(2, "转发"),
    CANCEL(3, "取关");

    private int code;

    private String message;

    DynamicTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
