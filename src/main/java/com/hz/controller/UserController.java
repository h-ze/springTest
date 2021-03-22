package com.hz.controller;

import com.hz.entity.ConvertResult;
import com.hz.entity.ResultMap;
import com.hz.entity.User;
import com.hz.service.UserService;
import com.hz.utils.JWTUtil;
import com.hz.utils.JWTUtils;
import com.hz.utils.SaltUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@Api(tags = "swagger 用户管理接口")
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ResultMap resultMap;

    //@ResponseBody
    @GetMapping("/findAll")
    public String getAll(Model model){
        List<User> all = userService.findAll();
        model.addAttribute("users", all);
        logger.info("info信息");
        return "showALl";
    }

    @GetMapping("/save")
    //@ResponseBody
    public String save(User user){
        userService.save(user);
        return "redirect:/user/findAll";
    }

    @PostMapping(value = "/addUser")
    @ResponseBody
    public ConvertResult addUser(@RequestParam("username") String username , @RequestParam("password") String password){
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
            List<Object> list = new ArrayList<>();
            //list.add("user");
            //addUser.setRoles(null);
            int i = userService.save(addUser);
            if (i >0){
                return new ConvertResult(0,"添加成功","用户已添加");
            }else {
                return new ConvertResult(0,"添加失败","用户添加失败");
            }
        }
    }

    @GetMapping("findAllJsp")
    public String findAllJsp(HttpServletRequest request, Model model){
        model.addAttribute("name","heze");
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("findAllByGThemeleaf")
    public String findAll(HttpServletRequest request,Model model){
        model.addAttribute("name","heze");
        model.addAttribute("username","<a href=''>test </a>");
        //List<User> users = Arrays.asList(new User("1", "zhangsan", 24, new Date()), new User("2", "lisi", 24, new Date()));
        //model.addAttribute("users", users);
        List<User> users = Arrays.asList(new User(1, "zhangsan", 24, new Date()), new User(2, "lisi", 24, new Date()));

        model.addAttribute("user", new User(1, "zhangsan", 22, new Date()));
        model.addAttribute("users", users);

        return "index";
    }

    @GetMapping("delete")
    @ResponseBody
    public String deleteUser(String id,String name){
        return "删除id为"+id+" 姓名为"+name;
    }


    //使用shiro
    @PostMapping("loginByShiro")
    @ResponseBody
    public ConvertResult loginByShiro(String username, String password){
        System.out.println(username);
        System.out.println(password);

        /*DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //2.给安全管理器设置realm
        ShiroCustomerRealm myCusttomer = new ShiroCustomerRealm();

        //对值进行hash及加密方式以及散列次数
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);

        myCusttomer.setCredentialsMatcher(hashedCredentialsMatcher);
        //securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        securityManager.setRealm(myCusttomer);

        //3.SecurityUtils 给全局安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);*/

        //4.关键对象subject主体
        /*Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

        try {
            System.out.println("认证状态："+subject.isAuthenticated());
            subject.login(usernamePasswordToken);
            System.out.println("认证状态："+subject.isAuthenticated());
            System.out.println(usernamePasswordToken);
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("用户名不存在");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }

        if(subject.isAuthenticated()){
            //1.基于角色权限控制
            boolean admin = subject.hasRole("admin");
            System.out.println("管理员"+admin);
            //logger.info(admin);
            System.out.println("权限"+subject.isPermitted("user:update"));

        }*/

        return new ConvertResult(0,"登录成功","获取用户信息成功");

        //User user = userService.getUser(username);

        /*Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

        try {
            System.out.println("认证状态："+subject.isAuthenticated());
            subject.login(usernamePasswordToken);
            System.out.println("认证状态："+subject.isAuthenticated());
            System.out.println(usernamePasswordToken);
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("用户名不存在");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }

        if(subject.isAuthenticated()){
            //1.基于角色权限控制
            boolean admin = subject.hasRole("admin");
            System.out.println("管理员"+admin);
            //logger.info(admin);
            System.out.println("权限"+subject.isPermitted("user:update"));
            //session.setAttribute("user",user);

            return "redirect:/file/showAllFile";
        }else {
            return "redirect:/index/login";
        }*/

//        if (user.getPassword().equals(password)){
//            session.setAttribute("user",user);
//            return "redirect:/file/showAllFile";
//        }else {
//            return "redirect:/index/login";
//        }
    }

    /**
     * 使用jwt
     * @param username 用户名
     * @param password 密码
     * @return ConvertResult对象
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public ConvertResult login(@RequestParam("username") String username , @RequestParam("password") String password){
        logger.info(username);
        logger.info(password);
        User user = userService.getUser(username);
        if (user!=null){
            String sha = SaltUtil.shiroSha(password ,user.getSalt());
            logger.info(sha);
            if (sha.equals(user.getPassword())){
                String token = jwtUtil.createJWT(user.getId().toString(),
                        user.getName(),password/*user.getPassword()*/, user.getSalt());
                logger.info(token);
                return new ConvertResult(0,"登录成功",token);
            }else {
                return new ConvertResult(999999,"登录失败,密码错误,请重新输入","");
            }
        }else {
            return new ConvertResult(999999,"登录失败,用户不存在","");
        }
    }

    @RequestMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "";
    }


    @PostMapping("getToken")
    @ApiOperation(value ="用户登录",notes="获取用户的token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",  dataType = "String",value = "用户名", defaultValue = "张三"),
            @ApiImplicitParam(name = "password", value = "用户密码", defaultValue = "123456", required = true)
    })
    public Map<String,Object> getToken(String username, String password){
        System.out.println(username);
        System.out.println(password);
        User user = userService.getUser(username);
        Map<String, Object> resultMap = new HashMap<>();

        if (user.getPassword().equals(password)){
            //session.setAttribute("user",user);
            try {
                Map<String, String> map = new HashMap<>();
                map.put("id",user.getId().toString());
                map.put("usernmae",user.getName());
                String token = JWTUtils.getToken(map);


                resultMap.put("token",token);
                resultMap.put("state",true);
                resultMap.put("msg","认证成功");
            } catch (Exception e) {
                e.printStackTrace();
                resultMap.put("msg",e.getMessage());
                resultMap.put("state",true);
            }

        }
        return resultMap;
    }

    @GetMapping(value = "testRoles")
    //@RequiresPermissions("")
    @ResponseBody
    public String testRoles(HttpServletRequest request){
        Claims calms = (Claims)request.getAttribute("claims");
//        Object roles = calms.get("roles");
        //logger.info("roles",roles);
        //logger.info(calms.toString());
        //User user = userService.getUser("test");

        //return new ConvertResult(0,"测试权限","权限测试成功");
        return "success";

    }

    @GetMapping(value = "testRoles1")
    //@RequiresPermissions("")
    @ResponseBody
    public ConvertResult testRoles1(HttpServletRequest request){
        //Claims calms = (Claims)request.getAttribute("claims");
//        Object roles = calms.get("roles");
        //logger.info("roles",roles);
        //logger.info(calms.toString());
        //User user = userService.getUser("test");

        return new ConvertResult(0,"测试权限1","权限测试成功");

    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }

}
