package com.hndt.vo;

import lombok.Data;

/**
 * 初始化页面需要的字段
 *
 * @author Hystar
 * @date 2018/3/19
 */
@Data
public class InitVo {
    /**
     * 频率名称
     */
    String channelName;
    /**
     * 登录名称
     */
    String loginName;
    /**
     * 登录头像
     */
    String loginIcon;
    /**
     * 总消息数量
     */
    int totalMessage;
    /**
     * 总的语音消息数量
     */
    int totalVoiceMessage;
    /**
     * 总的文本消息数量
     */
    int totalTextMessage;
    /**
     * 总的图片消息数量
     */
    int totalPicMessage;
}
