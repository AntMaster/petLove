package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum PublishTypeEnum {

    SEARCH_PET(1, "寻宠"),
    SEARCH_MASTER(2, "寻主");

    private int code;

    private String message;


    PublishTypeEnum(int code, String massage) {
        this.code = code;
        this.message = massage;

    }


}
