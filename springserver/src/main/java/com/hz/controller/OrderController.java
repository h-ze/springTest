package com.hz.controller;

import com.hz.demo.entity.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
@Api(tags = "订单管理接口")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("getOrder")
    @RequiresRoles(value = {"admin","user"}) //用来判断角色 同时具有admin user
    @RequiresPermissions("user:update:01") //用来判断权限字符串
    public void getOrder(){
        Subject subject = SecurityUtils.getSubject();
        subject.hasRole("admin");
    }

    @GetMapping("/c/get/{id}")
    @ApiOperation(value = "获取订单信息",notes = "获取订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "订单id",paramType = "path",dataType = "String",required = true)
    })
    public ResponseResult get(@PathVariable String id) {
        String forObject = restTemplate.getForObject("http://customer:8762/get/" + id, String.class);
        return new ResponseResult<>(100000, "查找订单信息成功", forObject);
    }
}
