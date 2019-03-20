package com.hndt.controller;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Role;
import com.hndt.service.RoleService;
import com.hndt.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Hystar
 * @date 2018/1/15
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取分页列表
     *
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "content") String content,
                               @RequestParam(value = "pageNum") Integer pageNum,
                               @RequestParam(value = "pageSize") Integer pageSize) {
        return roleService.listRole(StringUtils.isEmpty(content) ? null : content, pageNum, pageSize);
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @RequestMapping(value = "/list_check.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse listCheck() {
        return roleService.listCheck();
    }

    /**
     * 通过id删除一个角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(@RequestParam(value = "roleId") String roleId) {
        return roleService.deleteRoleById(roleId);
    }

    /**
     * 修改角色信息
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(Role role) {
        return roleService.updateRole(role);
    }

    /**
     * 获取角色详情信息
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(@RequestParam(value = "roleId") String roleId) {
        return roleService.detail(roleId);
    }

    /**
     * 添加一个角色
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insert(Role role) {
        return roleService.insertRole(role);
    }
}
