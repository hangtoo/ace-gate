package com.github.wxiaoqi.gate.back.controller;

import com.github.wxiaoqi.gate.back.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户权限
 *
 * @author wanghaobin
 * @create 2017-06-27 12:40
 */
@Controller
@RequestMapping("admin")
public class UserPermissionController {
    @Autowired
    private UserService userService;

    /**
     * 系统
     * 权限管理系统/内容管理系统
     *
     * @return
     */
    @RequestMapping(value = "/user/system", method = RequestMethod.GET)
    @ResponseBody
    public String getUserSystem() {
        return userService.getSystemsByUsername(getCurrentUserName());
    }


    /**
     * 获取用户的菜单
     */
    @ApiOperation(value = "获取用户的菜单", notes = "parentId父节点id")
    @RequestMapping(value = "/user/menu", method = RequestMethod.GET)
    @ResponseBody
    public String getUserMenu(@RequestParam(defaultValue = "-1") Integer parentId) {
        return userService.getMenusByUsername(getCurrentUserName(), parentId);
    }

    /**
     * 获取当前登录用户的账号
     *
     * @return
     */
    public String getCurrentUserName() {
        // 通过ThreadLocal来保存和传递用户登录信息
        // SecurityContextHolder的主要功能是将当前正在执行的thread与SecurityContext关联起来
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }
}
