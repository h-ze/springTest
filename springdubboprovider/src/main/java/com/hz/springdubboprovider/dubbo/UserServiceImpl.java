package com.hz.springdubboprovider.dubbo;


import com.hz.springdubbointerface.service.UserService;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "${dubbo.provider.UserInterfaces.version}",interfaceClass = UserService.class)
//@Service(version = "1.0.0",interfaceClass = UserInterface.class)
public class UserServiceImpl implements UserService {
    @Override
    public void setUser() {
        System.out.println("dubbo提供消息");
    }
}
