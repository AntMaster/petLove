package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum PetFindStateEnum {

    FOUND(1, "已找到"),
    NOT_FOUND(0, "未找到");

    private int code;

    private String message;

    PetFindStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
