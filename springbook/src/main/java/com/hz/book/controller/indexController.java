package com.hz.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test1")
public class indexController {

    @RequestMapping("/test")
    public String getTest(){
        return "调用springbook测试成功";
    }

}
