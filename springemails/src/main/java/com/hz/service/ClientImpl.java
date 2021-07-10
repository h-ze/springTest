package com.hz.service;


import org.springframework.stereotype.Component;

@Component
public class ClientImpl implements Client {
    @Override
    public String getTest() {
        return "熔断器启动了";
    }
}
