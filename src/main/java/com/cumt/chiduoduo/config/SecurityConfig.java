package com.cumt.chiduoduo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
////                .antMatchers("/").permitAll()
////                .antMatchers("/user/login").permitAll()
////                .anyRequest().authenticated()
//                .and()
//                .logout().permitAll()
//                .and()
//                .cors()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login");
//        //跨站伪造防护功能关闭
//        http.csrf().disable();
//    }

//}