package com.shumahe.pethome.Enums;

import lombok.Getter;

@Getter
public enum MemberTagsEnum {

    Volunteer(1, "自愿者"),
    VolunteerPro(2, "高级志愿者");

    private int code;

    private String message;

    MemberTagsEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
