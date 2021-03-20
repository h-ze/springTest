package com.hz.config;

import com.hz.entity.Role;
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
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.ListUtils;

import java.util.List;

/**
 * shiro的认证授权
 */
public class ShiroCustomerRealm extends AuthorizingRealm {

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("身份信息："+primaryPrincipal);
//        simpleAuthorizationInfo.addRole("admin");
//        simpleAuthorizationInfo.addStringPermission("user:update");
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User rolesByUsername = userService.findRolesByUsername(primaryPrincipal);

        //如果添加缓存之后在该方法下再次请求数据库将不会再向数据库发起请求
        userService.getUser("zhangsan");
        System.out.println(rolesByUsername);
        if (!ListUtils.isEmpty(rolesByUsername.getRoles())) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            rolesByUsername.getRoles().forEach(role->{
                simpleAuthorizationInfo.addRole(role.getName());
            });
            return simpleAuthorizationInfo;

        }
        return null;
    }


    /**
     * 身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //在工厂中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        System.out.println("结果："+userService);
        User user = userService.getUser(principal);
        String password = user.getPassword();
        if (user !=null) {
            return new SimpleAuthenticationInfo(user.getName(),password, new MyByteSource(user.getSalt()),this.getName());
        }
        return null;
    }
}
