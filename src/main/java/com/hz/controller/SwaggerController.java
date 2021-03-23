package com.hz.controller;

import com.hz.entity.User;
import com.hz.service.UserService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "swagger user接口")
@RequestMapping("/swagger")
public class SwaggerController {

    @Autowired
    private UserService userService;
    @PostMapping("/addUser")
    @ApiOperation(value ="添加用户的接口",notes="用来添加一个新的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",  dataType = "String",value = "用户名", defaultValue = "李四"),
            @ApiImplicitParam(name = "address", value = "用户地址", defaultValue = "深圳", required = true)
    }
    )
    public String addUser( String username, @RequestParam(required = true) String address) {
        System.out.println(username);
        User user = new User();
        user.setName("测试1");
        user.setAge(18);
        user.setBir(new Date());
        user.setPassword("123456");
        //userService.save(user);
        return "success";
        //return new RespBean();
    }

    @GetMapping("/findUserById")
    @ApiOperation("根据id查询用户的接口")
    @RequiresRoles("admin1")
    @ApiImplicitParam(name = "id", value = "用户id", defaultValue = "99", required = true)
    public Map<String,Object> getUserById(Integer id) {
        System.out.println(id);
        Map<String, Object> map = new HashMap<>();
        map.put("姓名","张三");
        map.put("性别","男");
        return map;
    }


    @PostMapping("/save")
    @ApiOperation("保存用户信息")
    @ApiResponses({@ApiResponse(code = 401,message = "未被授权"),
                    @ApiResponse(code =404,message = "路径错误")})

    //requestBody可以不加ApiImplicitParams
    public Map<String,Object> saveUser(@RequestBody User user) {
        System.out.println(user.getName());
        System.out.println(user.getAge());
        Map<String, Object> map = new HashMap<>();
        map.put("姓名","张三");
        map.put("性别","男");
        return map;
    }
}

