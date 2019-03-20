package com.hndt.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 消息
 *
 * @author Hystar
 * @date 2018/2/2
 */
@Data
public class Message {
    private Long id;
    /**
     * 第三方平台ID
     */
    private Long transactionId;
    /**
     * 消息来源
     */
    private String source;
    /**
     * 消息类型
     */
    private String fileType;
    /**
     * 频率ID
     */
    private Long channelId;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 语音转换成的汉字
     */
    private String addMsg;
    /**
     * 语音转换成的波形图
     */
    private String wave;
    /**
     * 语音总时长
     */
    private Integer totalDuration;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String icon;
    /**
     *
     */
    private String fromUid;
    /**
     * 微信openid
     */
    private String openId;

    private String unionId;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 所在国家
     */
    private String country;
    /**
     * 所在地区
     */
    private String province;
    /**
     * 消息状态：0-新消息 1-发布后的消息 2-推荐后的消息
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    public Message(Long id, Long transactionId, String source, String fileType, Long channelId, String content, String addMsg, String wave, Integer totalDuration, String nickname, String icon, String fromUid, String openId, String unionId, Integer gender, String city, String country, String province, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.transactionId = transactionId;
        this.source = source;
        this.fileType = fileType;
        this.channelId = channelId;
        this.content = content;
        this.addMsg = addMsg;
        this.wave = wave;
        this.totalDuration = totalDuration;
        this.nickname = nickname;
        this.icon = icon;
        this.fromUid = fromUid;
        this.openId = openId;
        this.unionId = unionId;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.province = province;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Message() {
        super();
    }

}