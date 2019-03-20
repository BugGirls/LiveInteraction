package com.hndt.enums;

import lombok.Getter;

/**
 * 返回结果枚举类
 *
 * @author Hystar
 * @date 2018/1/10
 */
@Getter
public enum ResultEnum {

    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数不正确"),
    PARAM_EMPTY(2, "参数为空"),
    NEED_LOGIN(10, "需要登录"),
    SERVER_ERROR(11, "服务器异常"),
    MESSAGE_EMPTY(20, "该消息不存在"),
    MESSAGE_TYPE_ERROR(21, "消息类型错误"),
    MESSAGE_IS_RECOMMEND(22, "该消息已经推荐")
    ;

    private int code;
    private String value;

    ResultEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
