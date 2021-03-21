package com.hz.config;

import com.hz.redis.RedisCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来整合shiro框架相关的配置项
 */
@Configuration
public class ShiroConfig {

    //1.创建shiroFilter
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //配置系统的受限资源
        
        //配置系统公共资源
        Map<String, String> map = new HashMap<String,String>();
        map.put("/user/login","anon");
        map.put("/index","authc");//authc 请求这个资源需要认证和授权
        //默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        
        return shiroFilterFactoryBean;

    }
    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getRealm") Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }

    //3.创建自定义realm
    @Bean
    //@Qualifier("getRealm")
    public Realm getRealm(){
        ShiroCustomerRealm shiroCustomerRealm = new ShiroCustomerRealm();
        //修改凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        shiroCustomerRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        //开启缓存管理
        //shiroCustomerRealm.setCacheManager(new EhCacheManager());
        shiroCustomerRealm.setCacheManager(new RedisCacheManager());
        shiroCustomerRealm.setCachingEnabled(true);//开启全局缓存
        shiroCustomerRealm.setAuthenticationCachingEnabled(true); //开启认证缓存
        shiroCustomerRealm.setAuthenticationCacheName("authenticationCache");
        shiroCustomerRealm.setAuthorizationCachingEnabled(true); //开启授权缓存
        shiroCustomerRealm.setAuthorizationCacheName("authorizationCache");

        return shiroCustomerRealm;
    }

    @Bean
    public void closeSession(){

    }

}
