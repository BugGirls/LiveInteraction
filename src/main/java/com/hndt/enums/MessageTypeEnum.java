package com.hndt.enums;

import lombok.Getter;

/**
 * 消息类型
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Getter
public enum  MessageTypeEnum {

    TEXT(1, "TEXT"),
    PICTURE(2, "PIC"),
    VOICE(3, "VOICE"),
    ;

    private Integer code;
    private String value;

    MessageTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
