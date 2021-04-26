package com.hz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/test1")
public class indexController {

    @Autowired
    private Client client;

    @GetMapping("/test1")
    public String getTest(){
        return client.getTest();
    }
}
