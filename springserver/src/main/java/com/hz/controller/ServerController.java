package com.hz.controller;


import com.hz.demo.entity.ResponseMessageWithoutException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hz.demo.entity.ResponseMessageWithoutException.successResult;


/**
 * 服务端
 */
@RestController
@Api(tags = "远程服务接口")
@RequestMapping("/server")
public class ServerController {

    private static final Logger logger =LoggerFactory.getLogger(ServerController.class);
    @GetMapping("/server")
    @ApiOperation(value ="当前服务器时间",notes="获取当前服务器时间")
    public ResponseMessageWithoutException<String> getServerTime(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info(format.format(date));
        return successResult(0,format.format(date),"获取当前服务器时间成功");
    }
}
