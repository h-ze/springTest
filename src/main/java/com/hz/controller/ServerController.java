package com.hz.controller;


import com.hz.entity.ConvertResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "远程服务接口")
@RequestMapping("/server")
public class ServerController {

    @GetMapping("/server")
    @ApiOperation(value ="当前服务器时间",notes="获取当前服务器时间")
    public ConvertResult getServerTime(){
        return new ConvertResult(0,"获取当前服务器时间成功","时间已️获取");
    }
}
