package com.ali.pay.config;
import com.alibaba.fastjson.JSONObject;
import com.ali.pay.entity.RestBean;
import com.ali.pay.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


/***可以在此基础上配置登录、注销、权限控制、会话管理
 * @Configuration 是Spring Framework提供的注解之一，它标记的类表示应用程序的Java配置类，用于配置Spring应用程序上下文的Bean。
 * @EnableWebSecurity 注解启用Web安全性，它是Spring Security框架提供的一个常用注解之一。它指示Spring Boot在应用程序上下文中启用Spring Security，并加载与Web安全性相关的Bean。
 */

@Configurable
@EnableWebSecurity
public class SecurityConfuguration {
    @Autowired
    private FrontendLoginEntryPoint frontendLoginEntryPoint;
    @Resource
    AuthorizeService authorizeService;
    //使用@Resource注解可以将一个对象引入到另一个对象中
    @Resource
    CorsConfigurationSource corsConfigurationSource;

    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling()
                .authenticationEntryPoint(frontendLoginEntryPoint)
                .and()
                .authorizeHttpRequests()
                //1.  anyRequest() ：匹配所有的请求，无论请求的路径是什么。该方法使用了fluent API的编程风格，允许我们在同一行代码中完成对请求的匹配和授权规则的配置。
                //2.  authenticated() ：该方法表示所有匹配到的请求都需要进行身份认证才能访问。如果请求的用户未经过身份认证，则请求将被重定向到登录页面。
                .antMatchers("/pay/*").permitAll() // 配置首页不需要验证
                .antMatchers("/commodities/*").permitAll() // 配置首页不需要验证
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/card-keys/*").permitAll()
                .antMatchers("/admin/*").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/coastline/login")
                .successHandler(this::onAuthenticationSuccess)
                .failureHandler(this::onAuthenticationFailure)
                .and()
                .logout()
                .logoutUrl("/api/coastline/logout")
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                .csrf()
                .disable()
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {

        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .and()
                .build();
    }


    private void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("utf-8");
        //这里是调用了fastjson工具来把json数据转换成string
        if (request.getRequestURI().endsWith("/login")) {
            response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, "登录失败")));
        } else if (request.getRequestURI().endsWith("/logout")) {
            SecurityContextHolder.clearContext();
            response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, "退出登录失败")));
        }

    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");

        if (request.getRequestURI().endsWith("/login")) {
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        } else if (request.getRequestURI().endsWith("/logout")) {
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setMaxAge(0); // 设置过期时间为0，会立即删除 Cookie
            SecurityContextHolder.clearContext();
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
        }
    }


}