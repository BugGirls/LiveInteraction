package com.hndt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.dao.PermissionMapper;
import com.hndt.dao.RoleMapper;
import com.hndt.enums.ResultEnum;
import com.hndt.pojo.Permission;
import com.hndt.pojo.Role;
import com.hndt.service.RoleService;
import com.hndt.util.JsonUtil;
import com.hndt.vo.RoleVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2018/1/15
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询参数获取分页信息
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> listRole(String content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Role> roleList = roleMapper.queryRoleListByParam(content);
        List<RoleVo> roleVoList = assembleRoleVoList(roleList);
        PageInfo pageInfo = new PageInfo<>(roleList);
        pageInfo.setList(roleVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private List<RoleVo> assembleRoleVoList(List<Role> roleList) {
        List<RoleVo> roleVoList = new ArrayList<>();

        for (Role role : roleList) {
            roleVoList.add(assembleRoleVo(role));
        }

        return roleVoList;
    }

    private RoleVo assembleRoleVo(Role role) {
        RoleVo roleVo = new RoleVo();

        roleVo.setStatus(role.getStatus());
        roleVo.setName(role.getName());
        roleVo.setId(role.getId());

        if (!StringUtils.isEmpty(role.getPermissionId())) {
            List<Long> permissionIdList = JsonUtil.toList(role.getPermissionId(), Long.class);
            List<Permission> permissionList = permissionMapper.queryListByIds(permissionIdList);
            roleVo.setPermissionList(permissionList);
        }

        return roleVo;
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @Override
    public ServerResponse listCheck() {

        List<Role> roleList = roleMapper.queryList();

        return ServerResponse.createBySuccess(roleList);
    }

    /**
     * 删除一个角色
     *
     * @param roleId
     * @return
     */
    @Override
    public ServerResponse deleteRoleById(String roleId) {
        // 1、非法参数判断
        if (StringUtils.isEmpty(roleId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        // 2、判断该角色是否存在
        Role role = roleMapper.selectByPrimaryKey(Long.parseLong(roleId));
        if (role == null) {
            return ServerResponse.createByErrorMessage("该角色不存在");
        }

        // 3、删除角色
        int deleteRow = roleMapper.deleteByPrimaryKey(Long.parseLong(roleId));
        if (deleteRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("角色删除失败");
        }
    }

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    @Override
    public ServerResponse updateRole(Role role) {
        if (role == null || role.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }


        int updateRow = roleMapper.updateByPrimaryKeySelective(role);
        if (updateRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("角色信息修改失败");
        }
    }

    /**
     * 通过ID获取角色详情信息
     *
     * @param roleId
     * @return
     */
    @Override
    public ServerResponse detail(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Role role = roleMapper.selectByPrimaryKey(Long.parseLong(roleId));
        if (role == null) {
            return ServerResponse.createByErrorMessage("该角色不存在");
        } else {
            RoleVo roleVo = assembleRoleVo(role);
            return ServerResponse.createBySuccess(roleVo);
        }
    }

    /**
     * 添加一个角色
     *
     * @param role
     * @return
     */
    @Override
    public ServerResponse insertRole(Role role) {
        if (role == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        int insertRow = roleMapper.insertSelective(role);
        if (insertRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("角色信息添加失败");
        }
    }

}
