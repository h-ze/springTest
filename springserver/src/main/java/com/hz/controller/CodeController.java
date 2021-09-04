package com.hz.controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.hz.ints.AccessLimit;
import com.hz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 二维码相关接口
 */

@RestController
@RequestMapping("code")
@Slf4j
public class CodeController {

    @Autowired
    private UserService userService;

    /**
     * 生成二维码
     * @param request
     * @param response
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = false)
    @GetMapping(value = "/getLoginQr")
    public void createCodeImg(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Controller","no-cache");

        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");

        String uuid = userService.createQrImg();
        log.info(uuid);
        response.setHeader("uuid",uuid);

        try {
            QrCodeUtil.generate(uuid,300,300,"jpg",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
