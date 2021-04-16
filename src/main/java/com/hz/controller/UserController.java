package com.hz.controller;

import com.hz.config.MqttPushClient;
import com.hz.entity.*;
import com.hz.service.UserService;
import com.hz.utils.HttpsUtils;
import com.hz.utils.JWTUtil;
import com.hz.utils.RedisUtil;
import com.hz.utils.SaltUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@Api(tags = "用户管理接口")
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ResultMap resultMap;

    @Autowired
    private HttpsUtils httpsUtils;

    //@ResponseBody
    @GetMapping("/findAll")
    public String getAll(Model model){
        List<User> all = userService.findAll();
        model.addAttribute("users", all);
        logger.info("info信息");
        return "showAll";
    }

    @GetMapping("/save")
    //@ResponseBody
    public String save(User user){
        userService.save(user,null);
        return "redirect:/user/findAll";
    }

    @GetMapping("findAllJsp")
    public String findAllJsp(HttpServletRequest request, Model model){
        model.addAttribute("name","heze");
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("findAllByGThymeleaf")
    public String findAll(HttpServletRequest request,Model model){
        model.addAttribute("name","heze");
        model.addAttribute("username","<a href=''>test </a>");
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));

        model.addAttribute("user", new User(1, "zhangsan", 22, new Date()));
        model.addAttribute("users", users);

        return "index";
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @return ConvertResult对象
     */
    @ApiOperation(value ="用户注册",notes="用来注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",dataType = "String",value = "用户名",required = true, paramType = "form"),
            @ApiImplicitParam(name = "password",dataType = "String",value = "用户密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "type",dataType = "String",value = "用户类型",required = true,paramType = "form")
    })
    @PostMapping(value = "/user")
    @ResponseBody
    public ConvertResult registerUser(String username , @RequestParam("password") String password,@RequestParam("type") Integer type){
        logger.info(username);
        logger.info(password);
        User user = userService.getUser(username);
        if (user!=null){
            return new ConvertResult(0,"添加失败","用户已存在");
        }else {
            User addUser = new User();
            addUser.setName(username);
            Map<String, String> result = SaltUtil.shiroSalt(password);
            addUser.setSalt(result.get("salt"));
            addUser.setPassword(result.get("password"));
            addUser.setBir(new Date());
            addUser.setAge(25);
            long l = System.currentTimeMillis();
            String value = String.valueOf(l);
            addUser.setUserId(value);
            logger.info("id:"+value);
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(value);
            userRoles.setRoleId(type);
            int i = userService.save(addUser,userRoles);
            if (i >0){
                return new ConvertResult(0,"注册成功","用户已注册成功");
            }else {
                return new ConvertResult(0,"注册失败","用户注册失败");
            }
        }
    }

    /**
     * 用户登录
     * 使用jwt 记录登录的时间 在调用接口时会判断在线时间 如果在线时间超过3天则强制删除token要求重新登录
     * @param username 用户名
     * @param password 密码
     * @return ConvertResult对象
     */
    /*multipart/form-data*/
    @PostMapping(value = "/login",consumes = "application/x-www-form-urlencoded")
    @ApiOperation(value ="用户登录",notes="获取用户的token",response = ConvertResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",dataType = "String",value = "用户名",required = true, paramType = "form"),
            @ApiImplicitParam(name = "password",dataType = "String",value = "用户密码", required = true, paramType = "form")
    })
    //@RequestPart
    @ResponseBody
    public ConvertResult login(@RequestParam("username") String username , @RequestParam("password")String password){
        logger.info(username);
        logger.info(password);
        User user = userService.getUser(username);
        if (user!=null){
            String sha = SaltUtil.shiroSha(password ,user.getSalt());
            logger.info(sha);
            if (sha.equals(user.getPassword())){
                String token = jwtUtil.createJWT(user.getId().toString(),
                        user.getName(),user.getUserId(), user.getSalt());
                logger.info(token);

                //将登录的token存储到redis中
                //boolean setRedisExpire = redisUtil.setRedisExpire(token, 600);
                //logger.info("结果:",setRedisExpire);
                return new ConvertResult(0,"登录成功",token);
            }else {
                return new ConvertResult(999999,"登录失败,密码错误,请重新输入","");
            }
        }else {
            return new ConvertResult(999999,"登录失败,用户不存在","");
        }
    }

    /**
     * 用户退出登录
     * @return ConvertResult对象
     */
    @ApiOperation(value ="退出登录",notes="使token过期")
    @PutMapping("/logout")
    @ResponseBody
    public ConvertResult logout() throws MqttException {
        Subject subject = SecurityUtils.getSubject();
        String subjectPrincipal = (String) subject.getPrincipal();
        logger.info("退出登录前的token:"+subjectPrincipal);
        subject.logout();
        String principal = (String)subject.getPrincipal();
        logger.info("退出登录的token:"+principal);

        //需要删除redis里的关于登录的key

        String kdTopic = "topic1";
        MqttPushClient.getInstance().publish(kdTopic, "稍微来点鸡血");
        //return new ResponseEntity<>("OK", HttpStatus.OK);

        return new ConvertResult(0,"退出登录","退出登录成功");
    }

    /**
     * 用户注销
     * @param password 密码 输入密码是为了二次验证 防止用户误删或被恶意删除
     * @return
     */
    @ApiOperation(value ="用户注销",notes="用来注销用户")
    @DeleteMapping("/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "用户密码",required = true, paramType="form")
    })
    @ResponseBody
    public ConvertResult deleteUser(String password){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        User user = userService.getUserByUserId(userId);
        if (user==null){
            return new ConvertResult(0,"删除失败","用户不存在");
        }
        String sha = SaltUtil.shiroSha(password ,user.getSalt());
        logger.info(sha);
        if (sha.equals(user.getPassword())){
            int i = userService.deleteUser(user.getUserId(), sha);
            if (i >0){
                //将redis中的信息删除
                //boolean setRedisExpire = redisUtil.setRedisExpire(token, 600);
                //logger.info("结果:",setRedisExpire);
                return new ConvertResult(0,"注销成功,如需帐号请重新注册","");
            }else {
                return new ConvertResult(0,"注销失败,请稍后重试","");
            }
        }else {
            return new ConvertResult(999999,"注销失败,密码错误,请重新输入","");
        }
    }

    /**
     * 修改密码
     * @param password 原密码
     * @param newPassword 新密码(前端要进行二次密码的比对)
     * @return
     */
    @PutMapping("/password")
    @ApiOperation(value ="修改用户密码",notes="用来修改用户的密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",  dataType = "String",value = "旧密码", paramType = "form",required = true),
            @ApiImplicitParam(name = "newPassword", dataType = "String",value = "新密码", paramType = "form",required = true)
    })
    @ResponseBody
    public ConvertResult updateUserPassword(String password,String newPassword){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        User user = userService.getUserByUserId(userId);
        if (user==null){
            return new ConvertResult(0,"修改密码失败","用户不存在");
        }
        String sha = SaltUtil.shiroSha(password ,user.getSalt());
        logger.info(sha);
        if (sha.equals(user.getPassword())){
            Map<String, String> result = SaltUtil.shiroSalt(newPassword);
            user.setSalt(result.get("salt"));
            user.setPassword(result.get("password"));
            int i = userService.updateUserPassword(user);
            if (i >0){
                //将redis中的信息删除或设置一个密码被修改的标识
                //boolean setRedisExpire = redisUtil.setRedisExpire(token, 600);
                //logger.info("结果:",setRedisExpire);
                return new ConvertResult(0,"密码修改成功,请重新登录","");
            }else {
                return new ConvertResult(0,"修改密码失败,请稍后重试","");
            }
        }else {
            return new ConvertResult(999999,"修改密码失败,请输入正确的密码","");
        }
    }

    @ApiOperation(value ="测试用户权限",notes="用来测试是否有管理员权限")
    @GetMapping(value = "testRoles")
    @RequiresRoles("admin")
    @ResponseBody
    public ConvertResult testRoles(){
        return new ConvertResult(0,"测试权限","权限测试成功");
    }

    @GetMapping(value = "test")
    @ResponseBody
    public ConvertResult test(){
        return new ConvertResult(0,"测试权限1","权限测试成功");
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }

    //body类型的参数
    //@RequestBody不能用@ApiImplicitParams注解,不会生效,应该使用 @ApiParam
    @ApiOperation(value ="编辑用户个人信息",notes="用来编辑用户个人信息")
    @PostMapping("/edit")
    @ResponseBody
    public ConvertResult updateUserMessage(@RequestBody() @ApiParam(name = "body",value = "用户个人信息",required = true)UserMessage userMessage){

        logger.info("用户信息:"+userMessage);
        String fullName = userMessage.getFullName();
        if (fullName ==null){
            return new ConvertResult(999999,"参数错误","fullName不能为空");
        }
        logger.info("用户信息："+fullName);
        return new ConvertResult(0,"修改成功","用户已修改");
    }

    @ApiOperation(value ="获取用户个人信息",notes="用来获取用户个人信息")
    @GetMapping("/edit")
    @ResponseBody
    public ResponseMessageWithoutException<User> getUserMessage(){
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        Claims claims = jwtUtil.parseJWT(principal);
        String userId = (String)claims.get("userId");
        User user = userService.getUserByUserId(userId);
        return new ResponseMessageWithoutException<>(0, "获取成功", user, "用户信息已获取");
    }

    @ApiOperation(value ="重置密码",notes="用户忘记密码之后使用邮箱或手机号进行密码重置")
    @PutMapping("/resetPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "用户邮箱",paramType = "query",dataType = "String",required = true)
    })
    public ConvertResult resetPassword(String email){
        logger.info("用户邮箱:"+email);
        return new ConvertResult(0,"重置成功","密码已重置,请前往邮箱点击重置链接生效重置操作");
    }

    @ApiOperation(value ="解绑qq或微信",notes="用户解绑第三方微信或qq快捷登录方式")
    @PutMapping(value = "/unbind")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "qq or wechat",paramType = "form",dataType = "string",allowableValues = "qq,wechat", allowMultiple = false,required = true)
    })
    public ConvertResult unbind(@RequestPart String type){
        logger.info("type:"+type);
        return new ConvertResult(0,"解绑成功","用户已解绑");
    }

    @ApiOperation(value ="验证用户是否已注册",notes="验证用户是否已注册")
    @GetMapping(value = "/exists")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email",value = "邮箱",paramType = "query",dataType = "String",required = true),
            @ApiImplicitParam(name = "phone_number",value = "手机号",paramType = "query",dataType = "String",required = true)

    })
    @ResponseBody
    public ConvertResult exists(@RequestParam("email") String email,@RequestParam("phone_number") String phone_number){
        logger.info("email:"+email);
        logger.info("phone_number:"+phone_number);
        HttpResult httpResult = httpsUtils.doGet(new Server("https://online.cpdf360.cn"), "https://online.cpdf360.cn:443/api/user/email-by-phone?access-token=607806b2b502a35b191b586c&phone_number=18236581750", null);
        logger.info("结果:"+httpResult.getResult());
        return new ConvertResult(0,"解绑成功","用户已解绑");
    }

}
