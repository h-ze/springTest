package com.hz.controller;


import com.hz.entity.ConvertResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operatorLog")
@Api(tags = "日志管理接口")
public class OperatorLogController {

    @PostMapping("/log")
    @ApiOperation(value ="添加用户的操作日志",notes="用来添加用户操作日志")
    @ApiImplicitParams({@ApiImplicitParam(name = "appName", paramType = "header", dataType = "String",value = "收信任的appName",required = true),
            @ApiImplicitParam(name = "appToken", paramType = "form", dataType = "String",value = "收信任的app名称的token",required = true),
            @ApiImplicitParam(name = "email", paramType = "form", dataType = "String",value = "用户email"),
            @ApiImplicitParam(name = "to_id", paramType = "form", dataType = "String",value = "被操作用户cas_id或被操作文档cDocId"),
            @ApiImplicitParam(name = "api", paramType = "form", dataType = "String",value = "api，例如：enterprise-group",required = true),
            @ApiImplicitParam(name = "method", paramType = "form", dataType = "String",value = "用户请求的方式，包括 get post put delete options",required = true),
            @ApiImplicitParam(name = "params", paramType = "form", dataType = "String",value = "用户请求的参数，参数之间以&分隔，格式：enterpirse =100&group=10"),
            @ApiImplicitParam(name = "url", paramType = "form", dataType = "String",value = "用户请求的完整url"),
            @ApiImplicitParam(name = "ip", paramType = "form", dataType = "String",value = "用户的ip，非cPDF360项目必传"),
            @ApiImplicitParam(name = "code", paramType = "form", dataType = "String",value = "用户操作的响应code"),
            @ApiImplicitParam(name = "msg", paramType = "form", dataType = "String",value = "用户操作的响应msg"),
            @ApiImplicitParam(name = "is_success", paramType = "form", dataType = "String",value = "用户操作是否成功"),
            @ApiImplicitParam(name = "note", paramType = "form", dataType = "String",value = "用户操作的描述"),

    }
    )
    public ConvertResult addLog(@RequestHeader("appName") String appName,
                                @RequestParam String appToken,
                                @PathVariable("email") String email,
                                @RequestParam String to_id,
                                @RequestParam String api,
                                @RequestParam String method,
                                @RequestParam String params,
                                @RequestParam String url,
                                @RequestParam String ip,
                                @RequestParam String code,
                                @RequestParam String msg,
                                @RequestParam boolean is_success,
                                @RequestParam String note){
        return new ConvertResult(0,"删除成功","用户已删除");
    }

    @GetMapping("/log")
    @ApiOperation(value ="获取用户的操作日志",notes="根据类型获取用户的操作日志")
    public ConvertResult getLog(int type, String keyrword, int page, int per_page){
        return new ConvertResult(0,"删除成功","用户已删除");
    }

    @DeleteMapping("/log")
    @ApiOperation(value ="删除用户的操作日志",notes="删除当前用户的操作日志")
    public ConvertResult deleteLog(int type, String keyrword, int page, int per_page){
        return new ConvertResult(0,"删除成功","用户已删除");
    }

}
