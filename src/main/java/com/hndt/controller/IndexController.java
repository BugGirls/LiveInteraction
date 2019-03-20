package com.hndt.controller;

import com.hndt.pojo.Manager;
import com.hndt.service.ManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author Hystar
 * @date 2018/1/9
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    @Resource
    private ManagerService managerService;

    @RequestMapping(value = "index.do")
    @ResponseBody
    public String index() {
        Manager manager = new Manager();
        manager.setId(1L);
        managerService.allowVisit(manager, "/index.do");

        return "success";
    }

    @RequestMapping(value = "new_message.do")
    public ModelAndView newMessage() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("pages/new_message");

        return modelAndView;
    }
}
