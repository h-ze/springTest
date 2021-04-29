package com.hz.springdubboconsumer.dubbo;


import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class UserInterfaceConsumer {

    @Reference(version = "1.0.0")
    private UserInterface userInterface;

    @GetMapping("/test")
    public void getUserController(){
        userInterface.getUser();
    }
}
