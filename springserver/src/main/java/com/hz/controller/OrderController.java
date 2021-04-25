package com.hz.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {

    @GetMapping("getOrder")
    @RequiresRoles(value = {"admin","user"}) //用来判断角色 同时具有admin user
    @RequiresPermissions("user:update:01") //用来判断权限字符串
    public void getOrder(){
        Subject subject = SecurityUtils.getSubject();
        subject.hasRole("admin");


    }
}
