package com.hndt.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Hystar
 * @date 2018/1/10
 */
@Data
public class MessageDTO {
    /**
     * 消息来源 最新、微信、APP、微博
     */
    private String source;
    /**
     * 消息类型 文本、图片、语音
     */
    private String type;
    /**
     * 内容
     */
    private String content;
    /**
     * 发信人
     */
    private String send;
    /**
     * 所属频率
     */
    private String channelId;
    /**
     * 所属栏目
     */
    private String columnId;
}
