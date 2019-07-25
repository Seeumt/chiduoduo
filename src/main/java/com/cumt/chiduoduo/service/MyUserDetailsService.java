package com.cumt.chiduoduo.service;

import com.cumt.chiduoduo.dao.UserMapper;
import com.cumt.chiduoduo.dao.UserPasswordMapper;
import com.cumt.chiduoduo.model.User;
import com.cumt.chiduoduo.model.UserPassword;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

//@Component
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Resource
//    private UserMapper userMapper;
//    @Resource
//    private UserPasswordMapper userPasswordMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userMapper.selectByUserName(username);
//        UserPassword userPassword = userPasswordMapper.selectByUserId(user.getId());
//        return new org.springframework.security.core.userdetails.User(
//                username, userPassword.getPassword(), true, true, true, true,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("admin")
//        );
//    }
//}
