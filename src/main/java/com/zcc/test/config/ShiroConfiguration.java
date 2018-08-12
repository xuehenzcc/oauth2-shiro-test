package com.zcc.test.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.zcc.test.mapper.AccessMapper;
import com.zcc.test.model.AccessToken;
import com.zcc.test.repository.OAuthRSJdbcRepository;
import com.zcc.test.repository.OAuthRSRepository;
import com.zcc.test.service.OAuthRSServiceImpl;
import com.zcc.test.shiro.MyShiroRealm;
import com.zcc.test.shiro.OAuth2SubjectFactory;
import com.zcc.test.shiro.filter.OAuth2Filter;

@Configuration
public class ShiroConfiguration {

	//将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }
    @Bean
    public OAuth2Filter myFilter() {
    	OAuth2Filter filter = new OAuth2Filter();
    	filter.setRsService(new OAuthRSServiceImpl());
    	filter.setResourceId("os-resource");
        return filter;
    }
    @Bean
    public OAuth2SubjectFactory mySubjectFactory() {
    	OAuth2SubjectFactory myShiroRealm = new OAuth2SubjectFactory();
        return myShiroRealm;
    }
    @Bean
    public MemoryConstrainedCacheManager memoryConstrainedCacheManager(){
    	return new MemoryConstrainedCacheManager();
    }
    
    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(memoryConstrainedCacheManager());
        securityManager.setSubjectFactory(mySubjectFactory());
        return securityManager;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    
    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager,OAuth2Filter myFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,Filter> mapFilter = new HashMap<String, Filter>();
        mapFilter.put("aaa",myFilter );
        shiroFilterFactoryBean.setFilters(mapFilter);
        Map<String,String> map = new HashMap<String, String>();
        //登出
//        map.put("/logout","logout");
        map.put("/zcc/**","aaa");
        map.put("/api/**","aaa");
        //对所有用户认证
//        map.put("/**","authc");
        map.put("/**","anon");
        //登录
//        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    
}
