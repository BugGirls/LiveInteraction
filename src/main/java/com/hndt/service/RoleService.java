package com.hndt.service;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Role;
import com.hndt.vo.RoleVo;

/**
 * @author Hystar
 * @date 2018/1/15
 */
public interface RoleService {

    /**
     * 通过查询参数获取分页信息
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> listRole(String content, int pageNum, int pageSize);

    /**
     * 获取所有角色
     *
     * @return
     */
    ServerResponse listCheck();

    /**
     * 删除一个角色
     *
     * @param roleId
     * @return
     */
    ServerResponse deleteRoleById(String roleId);

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    ServerResponse updateRole(Role role);

    /**
     * 通过ID获取角色详情信息
     *
     * @param roleId
     * @return
     */
    ServerResponse detail(String roleId);

    /**
     * 添加一个角色
     *
     * @param role
     * @return
     */
    ServerResponse insertRole(Role role);
}
