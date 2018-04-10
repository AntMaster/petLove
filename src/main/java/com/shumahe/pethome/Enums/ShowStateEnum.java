package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum ShowStateEnum {

    HIDE(0, "隐藏"),
    SHOW(1, "显示");

    private int code;
    private String msg;


    ShowStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
