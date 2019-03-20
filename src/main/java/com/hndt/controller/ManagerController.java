package com.hndt.controller;

import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Manager;
import com.hndt.service.ManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户 Controller
 *
 * @author Hystar
 * @date 2018/1/10
 */
@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    @Resource
    private ManagerService managerService;

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
    public ServerResponse list(@RequestParam(value = "content") String content,
                                         @RequestParam(value = "pageNum") Integer pageNum,
                                         @RequestParam(value = "pageSize") Integer pageSize) {
        return managerService.listManager(StringUtils.isEmpty(content) ? null : content, pageNum, pageSize);
    }

    /**
     * 通过id删除一个用户
     *
     * @param managerId
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(@RequestParam(value = "managerId") String managerId) {
        return managerService.deleteManagerById(managerId);
    }

    /**
     * 修改用户信息
     *
     * @param manager
     * @return
     */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(Manager manager) {
        return managerService.updateManager(manager);
    }

    /**
     * 获取用户详情信息
     *
     * @param managerId
     * @return
     */
    @RequestMapping(value = "/detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(@RequestParam(value = "managerId") String managerId) {
        return managerService.detail(managerId);
    }

    /**
     * 获取session中的栏目信息列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/load_column_list.do", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> loadColumnList(HttpServletRequest request) {
        List<Map<String, Object>> columnList = (List<Map<String, Object>>) request.getSession().getAttribute("columnList");
        return columnList;
    }

    /**
     * 添加一个用户
     *
     * @param manager
     * @return
     */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insert(Manager manager) {
        return managerService.insertManager(manager);
    }

    /**
     * 获取用户中心用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping(value = "/user_center_list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse userCenterList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                              @RequestParam(value = "name", required = false) String name) {
        return ServerResponse.createBySuccess(managerService.userCenterList(name, pageNum, pageSize));
    }

    /**
     * 将用户中心的用户添加到本地数据库
     *
     * @param manager
     * @return
     */
    @RequestMapping(value = "/user_center_to_local.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse userCenterToLocal(Manager manager) {
        return managerService.userCenterToLocal(manager);
    }

    /**
     * 获取登录用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpServletRequest request) {
        Manager manager = (Manager) request.getSession().getAttribute(Const.LITERAL_MANAGER);
        return ServerResponse.createBySuccess(manager);
    }

    /**
     * 加载登录用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "load_login_message.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse loadLoginMessage(HttpServletRequest request) {
        Manager manager = (Manager) request.getSession().getAttribute(Const.LITERAL_MANAGER);
        return ServerResponse.createBySuccess(manager.getIcon());
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "log_out.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse logout(HttpServletRequest request) {
        request.getSession().removeAttribute(Const.LITERAL_MANAGER);
        return ServerResponse.createBySuccess();
    }

}
