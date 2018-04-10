package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum PublishStateEnum {

    SHOW(1, "发布显示"),
    HIDE(2, "发布隐藏");

    private int code;
    private String message;

    PublishStateEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
