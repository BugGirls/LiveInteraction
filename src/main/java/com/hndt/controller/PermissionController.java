package com.hndt.controller;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Permission;
import com.hndt.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 权限 Controller
 *
 * @author Hystar
 * @date 2018/1/12
 */
@Controller
@RequestMapping(value = "/permission")
public class PermissionController {


    @Resource
    private PermissionService permissionService;

    /**
     * 通过查询参数获取分页信息
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize,
                                         @RequestParam(value = "content") String name) {
        return permissionService.list(StringUtils.isEmpty(name) ? null : name, pageNum, pageSize);
    }

    /**
     * 获取所有信息
     *
     * @return
     */
    @RequestMapping(value = "/list_check.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse listCheck() {
        return permissionService.listCheck();
    }

    /**
     * 通过id删除一条权限
     *
     * @param permissionId
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(@RequestParam(value = "permissionId") String permissionId) {
        return permissionService.deleteById(permissionId);
    }

    /**
     * 修改权限信息
     *
     * @param permission
     * @return
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(Permission permission) {
        return permissionService.updatePermission(permission);
    }

    /**
     * 获取详情信息
     *
     * @param permissionId
     * @return
     */
    @RequestMapping(value = "/detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(@RequestParam(value = "permissionId") String permissionId) {
        return permissionService.detail(permissionId);
    }

    /**
     * 添加一条权限
     *
     * @param permission
     * @return
     */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insert(Permission permission) {
        return permissionService.insert(permission);
    }
}
