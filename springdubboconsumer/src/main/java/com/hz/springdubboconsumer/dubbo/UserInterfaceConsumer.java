package com.hz.springdubboconsumer.dubbo;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.catalina.User;

public class UserInterfaceConsumer {
    @Reference(version = "1.0.0")
    private UserInterface userInterface;

    public void getUserController(){
        userInterface.getUser();
    }
}
