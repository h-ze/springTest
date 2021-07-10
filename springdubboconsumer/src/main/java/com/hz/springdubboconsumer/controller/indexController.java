package com.hz.springdubboconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hz.springdubbointerface.service.UserInterfaces;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class indexController {

    /*@Reference(version = "1.0.0")
    private UserInterfaces userInterfaces;

    @GetMapping("test")
    public void test(){
        System.out.println(userInterfaces);
        userInterfaces.getUser();
    }*/

}
