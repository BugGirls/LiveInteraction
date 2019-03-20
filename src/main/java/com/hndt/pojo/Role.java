package com.hndt.pojo;

import lombok.Data;

/**角色
 *
 * @author Hystar
 * @date 2018/1/9
 */
@Data
public class Role {
    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 所拥有的权限
     */
    private String permissionId;
    /**
     * 状态 可用、不可用
     */
    private Integer status;

    public Role() {

    }

    public Role(Long id, String name, String permissionId, Integer status) {
        this.id = id;
        this.name = name;
        this.permissionId = permissionId;
        this.status = status;
    }
}