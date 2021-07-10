package com.hz.eurekaconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RoleController {

    // 这里配置的是我们要调用的服务实例名，我们要调用USER服务，因此这里的地址是USER
    private String rest_url_prefix = "http://springbootbook:8085";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("roles/{id}")
    public String getRole(@PathVariable("id") String id) {
        System.out.println("接收到请求[/roles/" + id + "]");

        // 调用USER服务中的/users/{id}服务
        return restTemplate.getForObject(rest_url_prefix + "/users/" + id, String.class);
    }

}