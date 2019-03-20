package com.hndt.dao;

import com.hndt.pojo.Permission;
import com.hndt.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    /**
     * 通过参数获取权限列表
     *
     * @param name
     * @return
     */
    List<Permission> queryListByParam(@Param(value = "name") String name);

    /**
     * 获取指定role_id下的所有权限
     *
     * @param roleId
     * @return
     */
    List<Permission> queryListByRoleId(@Param(value = "roleId") Long roleId);

    /**
     * 获取所有权限信息
     *
     * @return
     */
    List<Permission> queryList();

    /**
     * 通过id获取信息列表
     *
     * @param permissionIdList
     * @return
     */
    List<Permission> queryListByIds(@Param(value = "permissionIdList") List<Long> permissionIdList);
}