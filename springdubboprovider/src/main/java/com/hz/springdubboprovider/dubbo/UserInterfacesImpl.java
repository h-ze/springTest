package com.hz.springdubboprovider.dubbo;


import com.hz.springdubbointerface.service.UserInterfaces;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "${dubbo.provider.UserInterface.version}",interfaceClass = UserInterfaces.class)
//@Service(version = "1.0.0",interfaceClass = UserInterface.class)
public class UserInterfacesImpl implements UserInterfaces {
    @Override
    public void setUser() {
        System.out.println("dubbo提供消息");
    }
}
