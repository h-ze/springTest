package com.hz.controller;


import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {



    @ResponseBody
    @ExceptionHandler
    public Map<String,Object> handleException(Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("code","4000004");
        map.put("msg","发生了未可知的错误");
        map.put("message",e.getMessage());
        return map;
    }

    @ResponseBody
    @ExceptionHandler(ShiroException.class)
    public Map<String,Object> handleException(ShiroException e){
        Map<String,Object> map = new HashMap<>();
        map.put("code","999999");
        map.put("msg","发生了未可知的错误");
        map.put("message",e.getMessage());
        return map;
    }
}
