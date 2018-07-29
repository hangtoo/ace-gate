package com.github.wxiaoqi.gate.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 权限
 *
 * @author wanghaobin
 * @create 2017-06-02 13:43
 */
@Controller
public class SecurityController {
    /**
     * 登录
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }
}
