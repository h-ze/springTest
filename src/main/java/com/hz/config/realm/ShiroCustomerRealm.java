package com.hz.config.realm;

import com.hz.entity.User;
import com.hz.service.UserService;
import com.hz.utils.ApplicationContextUtils;
import com.hz.utils.JWTToken;
import com.hz.utils.JWTUtil;
import com.hz.utils.MyByteSource;
import io.jsonwebtoken.Claims;
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

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
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

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");
        return simpleAuthorizationInfo;

        //根据数据库中的用户权限进行添加
        /*UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        Claims claims = JWTUtil.parseJWT(primaryPrincipal);
        String subject = claims.getSubject();
        User rolesByUsername = userService.findRolesByUsername(subject);
        logger.info("用户:"+rolesByUsername);
        //如果添加缓存之后在该方法下再次请求数据库将不会再向数据库发起请求
        if (!ListUtils.isEmpty(rolesByUsername.getRoles())) {
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            rolesByUsername.getRoles().forEach(role-> simpleAuthorizationInfo.addRole(role.getName()));
            simpleAuthorizationInfo.addRole("admin");
            return simpleAuthorizationInfo;
        }
        return null;*/
    }

    /**
     * 身份认证 shiro的相关认证会自动跳到这个方法里
     * @param authenticationToken AuthenticationToken对象
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //在工厂中获取service对象
        logger.info("结果:"+principal);
        Claims claims = JWTUtil.parseJWT(principal);
        String username = claims.getSubject();
        logger.info("username:"+username);
        if (username == null) {
            throw new AuthenticationException("token认证失败！");
        }
        /*String password = userMapper.getPassword(username);
        if (password == null) {
            throw new AuthenticationException("该用户不存在！");
        }
        int ban = userMapper.checkUserBanStatus(username);
        if (ban == 1) {
            throw new AuthenticationException("该用户已被封号！");
        }*/

        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        logger.info("userService:"+userService);
        User user = userService.getUser(username);
        if (user ==null){
            throw new AuthenticationException("该用户不存在！");
        }
        String password = user.getPassword();
        logger.info("密码:"+password);

        //return new SimpleAuthenticationInfo(user.getName(),user.getPassword(), new MyByteSource(user.getSalt()),this.getName());

        return new SimpleAuthenticationInfo(principal, principal, this.getName());
    }
}
