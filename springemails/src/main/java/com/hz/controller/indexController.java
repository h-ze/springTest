package com.hz.controller;

import com.hz.service.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController()
@RequestMapping("/test1")
public class indexController {

    @Autowired
    private Client client;

    // 这里配置的是我们要调用的服务实例名，我们要调用springbook中的服务，因此这里的地址是springbook
    private String rest_url_prefix = "http://127.0.0.1:8085/springbootbook/test1/test1";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test1")
    public String getTest(){
        return client.getTest();
    }


    @GetMapping("/test2")
    public String getTest2(){
        return restTemplate.getForObject(rest_url_prefix, String.class);
    }
}
