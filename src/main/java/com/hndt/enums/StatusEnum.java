package com.hndt.enums;

import lombok.Getter;

/**
 * 状态枚举类
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Getter
public enum StatusEnum implements CodeEnum {

    NOT(0, "不可用"),
    OK(1, "可用"),
    ;

    private Integer code;
    private String value;

    StatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
