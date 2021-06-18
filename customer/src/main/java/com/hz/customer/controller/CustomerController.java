package com.hz.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    /**
     * restful风格接口
     * @param id 用户的id
     * @return 用户信息
     */
    @GetMapping("/get/{id}")
    public String get(@PathVariable String id){
        return "xieong"+id;
    }

    @GetMapping("/show")
    public String show(){
        return "顾客8762";
    }
}
