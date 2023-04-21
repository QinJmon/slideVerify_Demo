package com.verify.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/**").permitAll() // 放行所有请求
                .anyRequest().authenticated()
                .and().formLogin().disable() // 禁用默认登录页面
                .httpBasic().disable() // 禁用基本身份验证
                .csrf().disable(); // 禁用 CSRF 保护

        return http.build();
    }
}
