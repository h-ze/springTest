package com.hz.controller;


import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理逻辑
 */
@RestControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    //@ResponseBody
    @ExceptionHandler
    public Map<String,Object> handleException(Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("code","4000004");
        map.put("msg","发生了未可知的错误");
        map.put("message",e.getMessage());
        e.printStackTrace();
        return map;
    }

    //@ResponseBody
    @ExceptionHandler(ShiroException.class)
    public Map<String,Object> shiroHandleException(Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("code","999999");
        map.put("msg","认证失败");
        map.put("message",e.getMessage());
        return map;
    }



}
