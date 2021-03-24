package com.hz.controller;


import com.hz.entity.ConvertResult;
import com.hz.entity.User;
import com.hz.service.UserService;
import com.hz.utils.SaltUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    /**
     * 添加用户 管理员操作
     * @param username 添加的用户
     * @return
     */
    @PostMapping(value = "/addUser")
    @RequiresRoles("admin")
    public ConvertResult addUser(@RequestParam("username") String username){
        logger.info(username);
        User user = userService.getUser(username);
        if (user!=null){
            return new ConvertResult(0,"添加失败","用户已存在");
        }else {
            User addUser = new User();
            addUser.setName(username);
            String password="123456";
            Map<String, String> result = SaltUtil.shiroSalt(password);
            addUser.setSalt(result.get("salt"));
            addUser.setPassword(result.get("password"));
            addUser.setBir(new Date());
            addUser.setAge(25);
            int i = userService.save(addUser);
            if (i >0){
                return new ConvertResult(0,"添加成功","用户已添加");
            }else {
                return new ConvertResult(0,"添加失败","用户添加失败");
            }
        }
    }

    /**
     * 管理员删除用户
     * @param userId 用户的userId
     * @return
     */
    @DeleteMapping("/deleteUser")
    @RequiresRoles("admin")
    public ConvertResult deleteUser(String userId){
        int i = userService.deleteUser(userId);

        if (i >0){
            return new ConvertResult(0,"删除成功","用户已删除");
        }else {
            return new ConvertResult(0,"删除失败","用户删除失败");
        }
    }
}
