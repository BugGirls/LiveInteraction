package com.hndt.filter;

import com.hndt.common.Const;
import com.hndt.pojo.Manager;
import com.hndt.service.ManagerService;
import com.hndt.service.impl.ManagerServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 权限过滤器
 *
 * @author Hystar
 * @date 2018/1/9
 */
public class PrivilegeFilter extends OncePerRequestFilter {

    private static final String login_url = "http://uc.hndt.com/login.xhtml";
    private static final String error_url = "error.html";
    private static final String test_uri_start = "/test/";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setCharacterEncoding(Const.ENCODING_UTF8);
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (!uri.endsWith("/") && !uri.startsWith(test_uri_start) && !uri.contains(error_url) && !uri.contains("/api/manager/login.do")) {
            // 权限判断
            uri = uri.substring(contextPath.length());
            Manager manager = (Manager) session.getAttribute(Const.LITERAL_MANAGER);
            if (manager != null) {
                ManagerServiceImpl managerService = new ManagerServiceImpl();
                if (managerService.allowVisit(manager, uri) || uri.contains(login_url)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendRedirect(contextPath + error_url);
                }
            } else if (uri.length() != 1 && uri.contains(login_url)) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect(contextPath + login_url);
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
