package com.hz.controller;


import com.hz.demo.entity.ConvertResult;
import com.hz.demo.entity.ResponseResult;
import com.hz.demo.entity.User;
import com.hz.service.UserService;
import com.hz.utils.SaltUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@Api(tags = "管理员接口")
@RequestMapping("admin")
@RequiresRoles("admin")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    /**
     * 添加用户 管理员操作
     * @param username 添加的用户
     * @return ConvertResult对象
     */
    @ApiOperation(value ="添加用户",notes="管理员添加用户")
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
            int i = userService.save(addUser,null);
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
     * @return ConvertResult对象
     */
    @ApiOperation(value ="删除用户",notes="管理员删除用户")
    @DeleteMapping("/user")
    @RequiresRoles("admin")
    public ConvertResult deleteUser(String userId){
        int i = userService.deleteUser(userId);
        if (i >0){
            return new ConvertResult(0,"删除成功","用户已删除");
        }else {
            return new ConvertResult(0,"删除失败","用户删除失败");
        }
    }

    /**
     * 管理员冻结用户
     * @param userId 用户的userId
     * @return ConvertResult对象
     */
    @ApiOperation(value ="改变普通用户状态",notes="管理员改变普通用户状态，使用中或被冻结或被拒绝")
    @PutMapping("/freezeUser")
    @RequiresRoles("admin")
    public ConvertResult freezeUser(@RequestParam("userId") String userId,@RequestParam("type") int type){
        int i = userService.deleteUser(userId);
        if (i >0){
            return new ConvertResult(0,"冻结成功","用户已冻结");
        }else {
            return new ConvertResult(0,"冻结失败","用户冻结失败");
        }
    }

    /**
     * 转让管理员
     * @param userId 用户的userId
     * @return ConvertResult对象
     */
    @ApiOperation(value ="转让管理员给其他用户",notes="管理员转移管理员权限给他人，被转的账号必须存在且必须是当前企业下的账号")
    @PutMapping("/transferAdmin")
    @RequiresRoles("admin")
    public ConvertResult transferAdmin(String userId){
        int i = userService.deleteUser(userId);
        if (i >0){
            return new ConvertResult(0,"删除成功","用户已删除");
        }else {
            return new ConvertResult(0,"删除失败","用户删除失败");
        }
    }

    /**
     * 获取各状态用户列表
     * @param type 状态 任务状态 0=申请中;1=使用中;2=已拒绝;3=已冻结
     * @param keyword 搜索关键字
     * @param page 页码
     * @param per_page 每页数据量
     * @return ConvertResult对象
     */
    @ApiOperation(value ="用户列表",notes="获取各状态下的成员列表")
    @GetMapping("/account")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",dataType = "int",value = "任务状态 0=申请中;1=使用中;2=已拒绝;3=已冻结", paramType = "query"),
            @ApiImplicitParam(name = "keyword",dataType = "String",value = "关键词搜索 搜索email和full_name的并集", paramType = "query"),
            @ApiImplicitParam(name = "page",dataType = "int",value = "页码", paramType = "query"),
            @ApiImplicitParam(name = "per_page",dataType = "int",value = "每页数据量", paramType = "query")
    }
    )
    @RequiresRoles("admin")
    public ResponseResult<User> getAccount(Integer type, String keyword, Integer page, Integer per_page){
        return new ResponseResult<User>(0,"获取用户列表成功",new User());
    }


    /**
     * 自助激活成员邮箱
     * @param cas_id 用户的cas_id
     * @return ConvertResult对象
     */
    @ApiOperation(value ="激活成员邮箱",notes="自助激活成员邮箱，成员无需再次激活")
    @PostMapping("/active")
    @RequiresRoles("admin")
    public ConvertResult activeUser(String cas_id){
        return new ConvertResult(0,"删除成功","用户已删除");

    }

    @ApiOperation(value ="配置个人级别单双因子",notes="用来配置个人级别的单双因子，控制cas登录验证")
    @PutMapping("/verify")
    @RequiresRoles("admin")
    public ConvertResult verifyCas(Integer type,String keyword,Integer page,Integer per_page){
        return new ConvertResult(0,"删除成功","用户已删除");

    }

}
