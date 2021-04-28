package com.hz.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// @FeignClient注解用于指定从哪个服务中调用功能 ，注意 里面的名称与被调用的服务名保持一致，并且不能包含下划线
@FeignClient(value = "springbootbook",path = "springbootbook/test1",fallback = ClientImpl.class)
@Component
public interface Client {
    @GetMapping("/test")
    String getTest();
}
