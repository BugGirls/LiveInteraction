package com.hndt.controller;

import com.google.gson.reflect.TypeToken;
import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.dao.ManagerMapper;
import com.hndt.dao.RoleMapper;
import com.hndt.enums.ChannelNameEnum;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Manager;
import com.hndt.pojo.Role;
import com.hndt.service.ManagerService;
import com.hndt.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户登录 Controller
 * 对接到河南广播网用户中心
 *
 * @author Hystar
 * @date 2018/1/17
 */
@Controller
@RequestMapping(value = "/live_interaction/api/manager")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ManagerService managerService;

    @Resource
    private ManagerMapper managerMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 验证token是否有效
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/login.do")
    public String checkToken(HttpServletRequest request) {
        String result = "";
        String queryUri = request.getRequestURI() + "?" + request.getQueryString();
        long timestamp = NumberUtils.toLong(request.getParameter("time"), 0L);

        // 5min有效期
        if (System.currentTimeMillis() - timestamp < Const.EXPIRATION_TIME) {
            // 获取参数中的appID
            String appId = HttpServletRequestUtil.getString(request, "appID");
            // 验证是否是自己的appId
            if (appId.equals(Const.APP_ID)) {
                // 如果uri中有项目名，则去掉
                if (queryUri.indexOf(Const.PROJECT_NAME) >= 0) {
                    int length = Const.PROJECT_NAME.length();
                    queryUri = queryUri.substring(length, queryUri.length());
                }
                String token = HttpServletRequestUtil.getString(request, "token");
                String sign = queryUri.replaceAll("&token=.*", String.format("&secret=%s", Const.APP_SECRET));
                sign = DigestUtils.sha1Hex(sign);
                // 验证通过
                if (sign.equals(token)) {
                    try {
                        String userId = HttpServletRequestUtil.getString(request, "id");
                        String userInfoUri = Const.USER_INFO_URI + "?user_id=" + userId;
                        userInfoUri = Base64Util.auth(userInfoUri);
                        String userStr = ClientUtil.clientJsonForGet(Const.DO_MAIN_USER_CENTER + userInfoUri);
                        if (StringUtils.isNotEmpty(userStr)) {
                            Map<String, Object> userDataMap = JsonUtil.toObject(userStr, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            Map<String, Object> userMap = (Map<String, Object>) userDataMap.get("data");
                            logger.info("userId={}", userId);
                            logger.info("用户登录信息：{}", JsonUtil.toJson(userMap));

                            String programId = HttpServletRequestUtil.getString(request, "program_id");
                            logger.info("programId={}", programId);
                            if (StringUtils.isNotEmpty(programId)) {
                                String programInfoUrl = Const.PROGRAM_INFO_URL + programId;
                                programInfoUrl = Base64Util.auth(programInfoUrl);
                                String programStr = ClientUtil.clientJsonForGet(programInfoUrl);
                                logger.info("programStr={}", programStr);
                                if (StringUtils.isNotEmpty(programStr)) {
                                    Map<String, Object> programMap = JsonUtil.toObject(programStr, new TypeToken<Map<String, Object>>() {
                                    }.getType());
                                    logger.info("该用户所在的频率信息：{}", programMap);

                                    // 将用户信息返回到session中
//                                List<Map<String, Object>> columnMap = (List<Map<String, Object>>) programMap.get("programs");
                                    request.getSession().setAttribute("channelName", programMap.get("name"));
//                                request.getSession().setAttribute("columnList", columnMap);

                                    Manager manager = managerMapper.selectByNameAndStatus((String) userMap.get("name"), StatusEnum.OK.getCode());
                                    logger.info("manager={}", manager);
                                    if (manager != null) {
                                        manager.setName((String) userMap.get("name"));
                                        manager.setIcon((String) userMap.get("icon"));
                                        manager.setChannelId(Long.parseLong(programId));
                                        managerService.updateManager(manager);
                                    } else {
                                        // 将返回的用户信息保存到本地数据库
                                        manager = new Manager();
                                        manager.setId(Long.parseLong(userId));
                                        manager.setName((String) userMap.get("name"));
                                        manager.setIcon((String) userMap.get("icon"));
                                        manager.setStatus(Integer.parseInt((String) userDataMap.get("status")));
                                        manager.setChannelId(Long.parseLong(programId));
                                        managerService.insertManager(manager);
                                    }
                                    request.getSession().setAttribute(Const.LITERAL_MANAGER, manager);

                                    if (manager.getRoleId() != null) {
                                        List<Long> roleIdList = JsonUtil.toList(manager.getRoleId(), Long.class);
                                        Role role = roleMapper.selectByPrimaryKey(roleIdList.get(0));
                                        if (role.getName().equals("管理员")) {
                                            result = "redirect:/pages/manager/permission_list.html";
                                        } else if (role.getName().equals("导播")) {
                                            result = "redirect:/pages/instructor/new_message.html";
                                        } else if (role.getName().equals("主持人")) {
                                            result = "redirect:/pages/presenter/list.html";
                                        } else {
                                            result = "redirect:/pages/error.html";
                                        }
                                    } else {
                                        logger.error("该用户没有分配权限，请管理员分配权限");
                                        result = "redirect:/pages/not_permission.html";
                                    }
                                }
                            } else {
                                logger.error("该用户不属于任何频率，请在用户中心分配频率");
                                result = "redirect:/pages/not_permission.html";
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = "redirect:/pages/error.html";
                    }
                } else {
                    result = "token不正确，非法请求";
                }
            } else {
                result = "AppID不正确，非法请求";
            }
        } else {
            result = "token失效";
        }

        return result;
    }

    /**
     * 临时登录
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/temp_login.do")
    public String TempLogin(HttpServletRequest request) {
        String result = "";

        String channelName = ChannelNameEnum.JIAO_YU.getKey();
        Manager manager = managerMapper.selectByPrimaryKey(448148724128940032L);
        request.getSession().setAttribute(Const.LITERAL_MANAGER, manager);
        request.getSession().setAttribute("channelName", channelName);

        if (manager.getRoleId() != null) {
            List<Long> roleIdList = JsonUtil.toList(manager.getRoleId(), Long.class);
            Role role = roleMapper.selectByPrimaryKey(roleIdList.get(0));
            if (role.getName().equals("管理员")) {
                result = "redirect:/pages/manager/permission_list.html";
            } else if (role.getName().equals("导播")) {
                result = "redirect:/pages/instructor/new_message.html";
            } else if (role.getName().equals("主持人")) {
                result = "redirect:/pages/presenter/list.html";
            } else {
                result = "redirect:/pages/error.html";
            }
        } else {
            logger.error("该用户没有分配权限，请管理员分配权限");
            result = "redirect:/pages/not_permission.html";
        }

        return result;
    }

}
