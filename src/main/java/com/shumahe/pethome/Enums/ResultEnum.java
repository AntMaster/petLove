package com.shumahe.pethome.Enums;

import lombok.Getter;

/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */
@Getter
public enum ResultEnum {

    FAILURE(0, "失败"),

    SUCCESS(1, "成功"),

    RESULT_EMPTY(2,"结果为空"),

    OPENID_EMPTY(3,"openId为空"),

    OPENID_ERROR(3,"openId不正确"),

    PARAM_ERROR(4, "参数不正确"),

    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),

    WECHAT_MP_ERROR(20, "微信公众账号方面错误"),

    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(21, "微信支付异步通知金额校验不通过"),


    LOGIN_FAIL(25, "登录失败, 登录信息不正确"),

    LOGOUT_SUCCESS(26, "登出成功"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
