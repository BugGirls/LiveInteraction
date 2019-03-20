package com.hndt.service;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Manager;

import java.util.Map;

/**
 * @author Hystar
 * @date 2018/1/9
 */
public interface ManagerService {
    /**
     * 通过查询参数获取分页信息
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> listManager(String content, int pageNum, int pageSize);
    /**
     * 删除一个用户
     *
     * @param managerId
     * @return
     */
    ServerResponse deleteManagerById(String managerId);
    /**
     * 修改用户信息
     *
     * @param manager
     * @return
     */
    ServerResponse updateManager(Manager manager);
    /**
     * 通过ID获取用户详情信息
     *
     * @param managerId
     * @return
     */
    ServerResponse detail(String managerId);

    /**
     * 通过登录名称获取用户信息
     *
     * @param name
     * @return
     */
    ServerResponse loadByName(String name);
    /**
     * 添加一个用户
     *
     * @param manager
     * @return
     */
    ServerResponse insertManager(Manager manager);

    /**
     * 获取用户中心B类用户信息
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> userCenterList(String name, int pageNum, int pageSize);

    /**
     * 将用户中心的用户添加到本地数据库
     *
     * @param manager
     * @return
     */
    ServerResponse userCenterToLocal(Manager manager);

    /**
     * 判断登录用户是否有访问uri的权限
     *
     * @param manager
     * @param uri
     * @return
     */
    boolean allowVisit(Manager manager, String uri);
}
