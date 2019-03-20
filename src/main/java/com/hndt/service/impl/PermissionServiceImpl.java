package com.hndt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.dao.PermissionMapper;
import com.hndt.enums.ResultEnum;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Permission;
import com.hndt.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Hystar
 * @date 2018/1/12
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询参数获取分页信息
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> list(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Permission> permissionList = permissionMapper.queryListByParam(name);
        PageInfo pageInfo = new PageInfo(permissionList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @Override
    public ServerResponse listCheck() {

        List<Permission> permissionList = permissionMapper.queryList();

        return ServerResponse.createBySuccess(permissionList);
    }

    /**
     * 删除一条权限
     *
     * @param permissionId
     * @return
     */
    @Override
    public ServerResponse deleteById(String permissionId) {
        // 1、非法参数判断
        if (StringUtils.isEmpty(permissionId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        // 2、判断该权限是否存在
        Permission permission = permissionMapper.selectByPrimaryKey(Long.parseLong(permissionId));
        if (permission == null) {
            return ServerResponse.createByErrorMessage("该权限不存在");
        }

        // 3、删除权限
        int deleteRow = permissionMapper.deleteByPrimaryKey(Long.parseLong(permissionId));
        if (deleteRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("权限删除失败");
        }
    }

    /**
     * 修改权限信息
     *
     * @param permission
     * @return
     */
    @Override
    public ServerResponse updatePermission(Permission permission) {
        if (permission == null || permission.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        int updateRow = permissionMapper.updateByPrimaryKeySelective(permission);
        if (updateRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("信息修改失败");
        }
    }

    /**
     * 获取详情信息
     *
     * @param permissionId
     * @return
     */
    @Override
    public ServerResponse detail(String permissionId) {
        if (StringUtils.isEmpty(permissionId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Permission permission = permissionMapper.selectByPrimaryKey(Long.parseLong(permissionId));
        if (permission == null) {
            return ServerResponse.createByErrorMessage("该权限不存在");
        } else {
            return ServerResponse.createBySuccess(permission);
        }
    }

    /**
     * 添加一条权限
     *
     * @param permission
     * @return
     */
    @Override
    public ServerResponse insert(Permission permission) {
        if (permission == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        int insertRow = permissionMapper.insertSelective(permission);
        if (insertRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("信息添加失败");
        }
    }
}
