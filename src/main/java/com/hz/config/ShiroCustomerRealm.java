package com.hz.config;

import com.hz.entity.User;
import com.hz.service.UserService;
import com.hz.utils.ApplicationContextUtils;
import com.hz.utils.MyByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.ListUtils;

/**
 * 自定义shiro的认证授权
 */
public class ShiroCustomerRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(ShiroCustomerRealm.class);
   // @Autowired
   //UserService userService ;
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        logger.info("身份信息："+primaryPrincipal);
//        simpleAuthorizationInfo.addRole("admin");
//        simpleAuthorizationInfo.addStringPermission("user:update");

        //
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User rolesByUsername = userService.findRolesByUsername(primaryPrincipal);

        //如果添加缓存之后在该方法下再次请求数据库将不会再向数据库发起请求
        //userService.getUser("zhangsan");
        //System.out.println(rolesByUsername);
        if (!ListUtils.isEmpty(rolesByUsername.getRoles())) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            rolesByUsername.getRoles().forEach(role-> simpleAuthorizationInfo.addRole(role.getName()));
            return simpleAuthorizationInfo;

        }
        return null;
    }


    /**
     * 身份认证 shiro的相关认证会自动跳到这个方法里
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //在工厂中获取service对象
        logger.info("结果:",principal);
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        logger.info("userService:"+userService);
        System.out.println("结果："+userService);
        User user = userService.getUser(principal);
        String password = user.getPassword();
        logger.info("密码:",password);
        return new SimpleAuthenticationInfo(user.getName(),password, new MyByteSource(user.getSalt()),this.getName());
    }
}
