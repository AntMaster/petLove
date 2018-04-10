package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum PetClassifyEnum {


    CAT(2, "猫"),
    DOG(3, "狗"),
    RABBIT(4, "兔子"),
    MOUSE(6, "老鼠");


    private int code;

    private String message;

    PetClassifyEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
