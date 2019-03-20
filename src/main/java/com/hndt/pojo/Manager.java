package com.hndt.pojo;

import lombok.Data;

/**
 * 用户
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Data
public class Manager {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 登录名
     */
    private String name;
    /**
     * 头像
     */
    private String icon;
    /**
     * 所属频率
     */
    private Long channelId;
    /**
     * 所拥有的角色
     */
    private String roleId;
    /**
     * 状态
     */
    private Integer status;

    public Manager() {
    }

    public Manager(Long id, String name, String icon, Long channelId, String roleId, Integer status) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.channelId = channelId;
        this.roleId = roleId;
        this.status = status;
    }


}