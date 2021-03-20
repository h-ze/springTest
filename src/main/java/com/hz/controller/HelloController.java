package com.hz.controller;

import com.hz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private User user;

    @RequestMapping("/sayHello")
    public String sayHello(){
        System.out.println(user);
        return "hello world";
    }
}
