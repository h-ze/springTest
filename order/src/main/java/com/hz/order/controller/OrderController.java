package com.hz.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//RestTemplate调用
//调用customer项目中的方法
@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/c/get/{id}")
    public String get(@PathVariable String id) {
        String forObject = restTemplate.getForObject("http://customer:8762/get/" + id, String.class);
        logger.info(forObject);
        return forObject;
    }

    @GetMapping("/show")
    public String show(){
        return "订单8763";
    }

}
