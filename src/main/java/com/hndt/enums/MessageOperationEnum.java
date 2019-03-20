package com.hndt.enums;

import lombok.Getter;

/**
 * @author Hystar
 * @date 2018/2/7
 */
@Getter
public enum MessageOperationEnum {

    ORIGINAL(0, "原始"),
    PUBLISH(1, "发布"),
    RECOMMEND(2, "推荐");

    private Integer code;
    private String value;

    MessageOperationEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
