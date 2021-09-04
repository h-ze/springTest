package com.hz.controller;

import com.hz.demo.entity.ResponseResult;
import com.hz.ints.AccessLimit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FangshuaController {

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping("/fangshua")
    @ResponseBody
    public ResponseResult<String> fangshua() {
        return new ResponseResult<>(100000, "请求成功", "请求成功");
    }
}
