package com.hndt.dao;

import com.hndt.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 通过查询参数获取列表信息
     *
     * @param content
     * @return
     */
    List<Role> queryRoleListByParam(@Param(value = "content") String content);

    List<Role> queryListByIds(@Param(value = "roleIdList") List<Long> roleIdList);

    List<Role> queryList();
}