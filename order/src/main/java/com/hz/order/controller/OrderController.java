package com.hz.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//RestTemplate调用
@RestController
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/c/get/{id}")
    public String get(@PathVariable String id) {
        String result = restTemplate.getForObject("http://customer:8762/get/"+id, String.class);
        return result;
    }

    @GetMapping("/show")
    public String show(){
        return "订单8763";
    }

}
