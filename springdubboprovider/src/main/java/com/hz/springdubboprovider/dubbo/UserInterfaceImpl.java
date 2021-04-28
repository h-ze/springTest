package com.hz.springdubboprovider.dubbo;


import com.alibaba.dubbo.config.annotation.Service;

@Service(version = "1.0.0")
public class UserInterfaceImpl implements UserInterface {
    @Override
    public void getUser() {
        System.out.println("dubbo提供消息");
    }
}
