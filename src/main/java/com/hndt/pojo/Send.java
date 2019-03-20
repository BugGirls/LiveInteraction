package com.hndt.pojo;

import lombok.Data;

/**
 * 发信人信息
 * @author Hystar
 * @date 2018/1/11
 */
@Data
public class Send {
    /**
     * 发信人ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 编号
     */
    private String nameNo;
    /**
     * 用户地址
     */
    private String area;

    public Send(Long id, String name, String headPortrait, String nickName, String nameNo, String area) {
        this.id = id;
        this.name = name;
        this.headPortrait = headPortrait;
        this.nickName = nickName;
        this.nameNo = nameNo;
        this.area = area;
    }

    public Send() {
        super();
    }

}