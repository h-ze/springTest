package com.hz.book.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {

    @GetMapping("users/{id}")
    public String getUser(@PathVariable("id") String id) {
        log.info("接收到请求[/users/"+id+"]");
        return "获取测试用户,id为:"+id;
    }

}