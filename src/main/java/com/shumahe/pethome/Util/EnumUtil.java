package com.shumahe.pethome.Util;

import com.shumahe.pethome.Enums.CodeEnum;

/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
