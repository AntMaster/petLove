package com.shumahe.pethome.Enums;


import lombok.Getter;

@Getter
public enum SearchEnum {

    NONE_VALUE(-1, "搜索页面未选择该项");

    private int code;

    private String message;

    SearchEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
