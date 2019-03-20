package com.hndt.service;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Permission;

/**
 * @author Hystar
 * @date 2018/1/12
 */
public interface PermissionService {
    /**
     * 通过查询参数获取分页信息
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(String name, int pageNum, int pageSize);

    /**
     * 删除一条权限
     *
     * @param permissionId
     * @return
     */
    ServerResponse deleteById(String permissionId);

    /**
     * 修改权限信息
     *
     * @param permission
     * @return
     */
    ServerResponse updatePermission(Permission permission);

    /**
     * 获取详情信息
     *
     * @param permissionId
     * @return
     */
    ServerResponse detail(String permissionId);

    /**
     * 添加一条权限
     *
     * @param permission
     * @return
     */
    ServerResponse insert(Permission permission);

    ServerResponse listCheck();
}
