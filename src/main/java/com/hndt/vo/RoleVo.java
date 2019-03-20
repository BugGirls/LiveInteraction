package com.hndt.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Permission;
import com.hndt.util.EnumUtil;
import lombok.Data;

import java.util.List;

/**
 * @author Hystar
 * @date 2018/1/15
 */
@Data
public class RoleVo {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 状态 可用、不可用
     */
    private Integer status;

    private List<Permission> permissionList;

    @JsonIgnore
    public String getStatusValue() {
        return EnumUtil.getByCode(status, StatusEnum.class).getValue();
    }
}
