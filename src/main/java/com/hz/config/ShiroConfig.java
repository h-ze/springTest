package com.hz.config;

import com.hz.config.filter.JWTFilter;
import com.hz.config.realm.ShiroCustomerRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
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
public class ShiroConfig {


    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

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
        logger.info("==================");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());

        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(filterMap);

        //配置系统的受限资源
        
        //配置系统公共资源
        Map<String, String> filterChainDefinitionMap = new HashMap<String,String>();
        //map.put("/user/login","anon");
        filterChainDefinitionMap.put("/**","jwt");
        //map.put("/**","authc");//authc 请求这个资源需要认证和授权
        //放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/user/login","anon");
        filterChainDefinitionMap.put("/unauthorized/**", "anon");

        //默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/user/testRoles");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }
    //2.创建安全管理器
    @Bean
    public SecurityManager getSecurityManager(Realm realm,SessionStorageEvaluator sessionStorageEvaluator){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
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
        logger.info("getRealm");
        ShiroCustomerRealm shiroCustomerRealm = new ShiroCustomerRealm();


        //如果在这里添加凭证校验匹配器 会和shirCustomerRealm中的默认匹配器匹配的发生冲突
        //修改凭证校验匹配器
        /*HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        shiroCustomerRealm.setCredentialsMatcher(hashedCredentialsMatcher);*/
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

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }



}
