package com.hz.springdubboprovider.dubbo;


import com.alibaba.dubbo.config.annotation.Service;
import com.hz.springdubbointerface.service.UserInterfaces;

@Service(version = "${dubbo.provider.UserInterface.version}",interfaceClass = UserInterfaces.class)
//@Service(version = "1.0.0",interfaceClass = UserInterface.class)
public class UserInterfacesImpl implements UserInterfaces {
    @Override
    public void getUser() {
        System.out.println("dubbo提供消息");
    }
}
