package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum PetSexEnum {


    MALE(0, "公"),
    FEMALE(1, "母");


    private int code;

    private String message;

    PetSexEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
