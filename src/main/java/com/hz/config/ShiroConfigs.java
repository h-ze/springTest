package com.hz.config;

import com.oracle.tools.packager.Log;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用来整合shiro框架相关的配置项
 * shiro的filter
 */
@Configuration
public class ShiroConfigs {


    private static final Logger logger = LoggerFactory.getLogger(ShiroConfigs.class);

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    //1.创建shiroFilter  定义这个之后工厂会直接进到这个类里 拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        logger.info("==================");

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //设置我们自定义的JWT过滤器
        //filterMap.put("jwt", new JWTFilter());

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //shiroFilterFactoryBean.setFilters(filterMap);

        //配置系统的受限资源
        
        //配置系统公共资源
        Map<String, String> map = new HashMap<String,String>();
        //map.put("/user/login","anon");
        //map.put("/index","authc");//authc 请求这个资源需要认证和授权

        //默认认证界面路径
        //shiroFilterFactoryBean.setLoginUrl("/login");
        //shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        logger.info("测试");
        return shiroFilterFactoryBean;

    }
    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager( Realm realm){
        Log.debug("test");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);

        //关闭shiro自带的session
        /*DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);*/
        return securityManager;
    }

    //3.创建自定义realm
    @Bean
    //@Qualifier("getRealm")
    public Realm getRealm(){
        Log.info("getRema");
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

        /*shiroCustomerRealm.setCacheManager(new RedisCacheManager());
        shiroCustomerRealm.setCachingEnabled(true);//开启全局缓存
        shiroCustomerRealm.setAuthenticationCachingEnabled(true); //开启认证缓存
        shiroCustomerRealm.setAuthenticationCacheName("authenticationCache");
        shiroCustomerRealm.setAuthorizationCachingEnabled(true); //开启授权缓存
        shiroCustomerRealm.setAuthorizationCacheName("authorizationCache");*/

        return shiroCustomerRealm;
    }



}
