package com.hndt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.dao.ManagerMapper;
import com.hndt.dao.PermissionMapper;
import com.hndt.dao.RoleMapper;
import com.hndt.enums.ResultEnum;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Manager;
import com.hndt.pojo.Permission;
import com.hndt.pojo.Role;
import com.hndt.service.ManagerService;
import com.hndt.util.Base64Util;
import com.hndt.util.ClientUtil;
import com.hndt.util.JsonUtil;
import com.hndt.vo.ManagerVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Hystar
 * @date 2018/1/9
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ManagerMapper managerMapper;

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
    public ServerResponse<PageInfo> listManager(String content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Manager> managerList = managerMapper.queryManagerListByParam(content);
        List<ManagerVo> managerVoList = assembleManagerVoList(managerList);
        PageInfo pageInfo = new PageInfo<>(managerList);
        pageInfo.setList(managerVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private List<ManagerVo> assembleManagerVoList(List<Manager> managerList) {
        List<ManagerVo> managerVoList = new ArrayList<>();

        managerList.forEach(manager -> managerVoList.add(assembleManagerVo(manager)));

        return managerVoList;
    }

    private ManagerVo assembleManagerVo(Manager manager) {
        ManagerVo managerVo = new ManagerVo();

        managerVo.setChannelId(manager.getChannelId());
        managerVo.setStatus(manager.getStatus());
        managerVo.setName(manager.getName());
        managerVo.setId(manager.getId() + "");
        managerVo.setIcon(manager.getIcon());
        managerVo.setRoleId(manager.getRoleId());

        if (manager.getRoleId() != null) {
            List<Long> roleIdList = JsonUtil.toList(manager.getRoleId(), Long.class);
            List<Role> roleList = roleMapper.queryListByIds(roleIdList);
            managerVo.setRoleList(roleList);
        }

        return managerVo;
    }

    /**
     * 删除一个用户
     *
     * @param managerId
     * @return
     */
    @Override
    public ServerResponse deleteManagerById(String managerId) {
        // 1、非法参数判断
        if (StringUtils.isEmpty(managerId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        // 2、判断该用户是否存在
        Manager manager = managerMapper.selectByPrimaryKey(Long.parseLong(managerId));
        if (manager == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        }

        // 3、删除用户
        int deleteRow = managerMapper.deleteByPrimaryKey(Long.parseLong(managerId));
        if (deleteRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("用户删除失败");
        }
    }

    /**
     * 修改用户信息
     *
     * @param manager
     * @return
     */
    @Override
    public ServerResponse updateManager(Manager manager) {
        if (manager == null || manager.getId() == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        int updateRow = managerMapper.updateByPrimaryKeySelective(manager);
        if (updateRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("用户信息修改失败");
        }
    }

    /**
     * 通过ID获取用户详情信息
     *
     * @param managerId
     * @return
     */
    @Override
    public ServerResponse detail(String managerId) {
        if (StringUtils.isEmpty(managerId)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Manager manager = managerMapper.selectByPrimaryKey(Long.parseLong(managerId));
        if (manager == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        } else {
            ManagerVo managerVo = assembleManagerVo(manager);
            return ServerResponse.createBySuccess(managerVo);
        }
    }

    /**
     * 通过登录名称获取用户信息
     *
     * @param name
     * @return
     */
    @Override
    public ServerResponse loadByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Manager manager = managerMapper.selectByName(name);
        if (manager == null) {
            return ServerResponse.createByErrorMessage("该用户不存在");
        } else {
            ManagerVo managerVo = assembleManagerVo(manager);
            return ServerResponse.createBySuccess(managerVo);
        }
    }

    /**
     * 添加一个用户
     *
     * @param manager
     * @return
     */
    @Override
    public ServerResponse insertManager(Manager manager) {
        if (manager == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        int insertRow = managerMapper.insertSelective(manager);
        if (insertRow > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("用户信息添加失败");
        }
    }

    /**
     * 获取用户中心B类用户信息
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> userCenterList(String name, int pageNum, int pageSize) {
        Map<String, Object> resultMap = new HashMap<>();

        String userListUri = Const.USER_LIST_B_URI;

        // 如果name字段为空，则获取所有B类用户，否则通过name获取该用户
        if (StringUtils.isEmpty(name)) {
            userListUri = Base64Util.auth(userListUri + "?page=" + pageNum + "&rows=" + pageSize);
            String userListStr = ClientUtil.clientJsonForGet(Const.DO_MAIN_USER_CENTER + userListUri);
            if (StringUtils.isNotEmpty(userListStr)) {
                resultMap = JsonUtil.toObject(userListStr, new TypeToken<Map<String, Object>>() {
                }.getType());
            }
        } else {
            userListUri = Base64Util.auth(userListUri);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("page", pageNum);
            paramMap.put("rows", pageSize);
            String json = JsonUtil.toJson(paramMap);
            String userListStr = ClientUtil.clientJson(json, Const.DO_MAIN_USER_CENTER + userListUri + "&name=" + name);
            if (StringUtils.isNotEmpty(userListStr)) {
                resultMap = JsonUtil.toObject(userListStr, new TypeToken<Map<String, Object>>() {
                }.getType());
            }
        }

        return resultMap;
    }

    /**
     * 将用户中心的用户添加到本地数据库
     *
     * @param manager
     * @return
     */
    @Override
    public ServerResponse userCenterToLocal(Manager manager) {
        if (manager == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Manager managerTemp = managerMapper.selectByName(manager.getName());
        if (managerTemp == null) {
            manager.setStatus(StatusEnum.OK.getCode());
            int insertRow = managerMapper.insert(manager);
            if (insertRow > 0) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("该用户添加到本地数据库失败");
            }
        } else {
            return ServerResponse.createBySuccess();
        }
    }

    /**
     * 判断登录用户是否有访问uri的权限
     *
     * @param manager
     * @param uri
     * @return
     */
    @Override
    public boolean allowVisit(Manager manager, String uri) {
        boolean allow = false;

        if (manager == null) {
            return allow;
        }

        List<Long> roleIdsList = JsonUtil.toList(manager.getRoleId(), Long.class);
        List<Role> roleList = roleMapper.queryListByIds(roleIdsList);
        List<Long> permissionIdsList = new ArrayList<>();
        for (Role role : roleList) {
            List<Long> ids = JsonUtil.toList(role.getPermissionId(), Long.class);
            permissionIdsList.addAll(ids);
        }
        List<Permission> permissionList = permissionMapper.queryListByIds(permissionIdsList);

        for (Permission permission : permissionList) {
            int match_index = permission.getUrl().indexOf('*');
            //不带*，严格匹配
            if (match_index == -1) {
                if (Objects.equals(permission.getUrl(), uri)) {
                    allow = true;
                    break;
                }
            }
            //*开头，如*.xhtml
            else if (match_index == 0) {
                String section = permission.getUrl().substring(1);
                if (uri.endsWith(section)) {
                    allow = true;
                    break;
                }
            }
            //*结尾 如/vlive/*
            else if (match_index == permission.getUrl().length() - 1) {
                String section = permission.getUrl().substring(0, match_index);
                if (uri.startsWith(section)) {
                    allow = true;
                    break;
                }
            }
            //*在中间 如/vlive/resp_*.xhtml
            else if (match_index > 0 && match_index < permission.getUrl().length() - 1) {
                String prefix = permission.getUrl().substring(0, match_index);
                String suffix = permission.getUrl().substring(match_index + 1, permission.getUrl().length());
                if (uri.startsWith(prefix) && uri.endsWith(suffix)) {
                    allow = true;
                    break;
                }
            }
        }

        return allow;
    }

}
