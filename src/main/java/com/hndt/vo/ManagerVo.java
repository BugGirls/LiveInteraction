package com.hndt.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Role;
import com.hndt.util.EnumUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Hystar
 * @date 2018/1/16
 */
@Data
public class ManagerVo {
    /**
     * 用户ID
     */
    private String id;
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
     * 频率名称
     */
    private String channelName;
    /**
     * 所拥有的角色
     */
    private String roleId;
    /**
     * 状态
     */
    private Integer status;

    private List<Role> roleList;

    /**
     * 该用户
     */
    private List<Map<String, Object>> columnList;

    @JsonIgnore
    public String getStatusValue() {
        return EnumUtil.getByCode(status, StatusEnum.class).getValue();
    }
}
