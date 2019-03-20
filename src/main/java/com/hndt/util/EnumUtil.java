package com.hndt.util;

import com.hndt.enums.CodeEnum;

/**
 * 通用工具枚举类
 *
 * @author Hystar
 * @date 2018/1/12
 */
public class EnumUtil {

    /**
     * 通过传入的code值，获取该code对应的value
     *
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
