package com.hndt.dao;

import com.hndt.pojo.Manager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Manager record);

    int insertSelective(Manager record);

    Manager selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Manager record);

    int updateByPrimaryKey(Manager record);

    /**
     * 通过查询参数获取信息列表
     *
     * @param content
     * @return
     */
    List<Manager> queryManagerListByParam(@Param(value = "content") String content);

    /**
     * 通过登录名称获取登录用户信息
     *
     * @param name
     * @return
     */
    Manager selectByName(@Param(value = "name") String name);

    /**
     * 通过id和状态获取消息信息
     *
     * @param name
     * @param status
     * @return
     */
    Manager selectByNameAndStatus(@Param(value = "name") String name, @Param(value = "status") Integer status);
}