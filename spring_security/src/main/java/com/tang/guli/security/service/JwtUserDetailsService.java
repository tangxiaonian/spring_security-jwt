package com.tang.guli.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Classname JwtUserDetailsService
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/5 16:39
 * @Created by ASUS
 */
public interface JwtUserDetailsService extends UserDetailsService {

    /**
     * 保存用户登录信息
     * @param user
     * @return
     */
    public String saveUserLoginInfo(UserDetails user);

}
