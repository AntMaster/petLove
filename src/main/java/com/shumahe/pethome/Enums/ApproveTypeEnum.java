package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum ApproveTypeEnum {

    PERSONAGE(1, "个人"),
    ASSOCIATION(2, "协会");

    private int code;

    private String message;

    ApproveTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
