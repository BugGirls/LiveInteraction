package com.hndt.enums;

import lombok.Getter;

/**
 * 消息来源
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Getter
public enum MessageSourceEnum {

    NEW("NEW", "最新"),
    WECHAT("WEI_XIN", "微信"),
    APP("HNGB_APP", "客户端"),
    WEIBO("WEI_BO", "微博")
    ;

    private String key;
    private String value;

    MessageSourceEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MessageSourceEnum keyOf(String key) {
        for (MessageSourceEnum messageSourceEnum : values()) {
            if (messageSourceEnum.getKey().equals(key)) {
                return messageSourceEnum;
            }
        }

        return null;
    }

}
