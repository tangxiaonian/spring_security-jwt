package com.tang.guli.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tang.guli.security.config.JwtAuthenticationToken;
import com.tang.guli.utils.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.NonceExpiredException;

/**
 * @Classname JwtAuthenticationProvider
 * @Description [ JwtAuthenticationManager---> 自定义provider验证token有效性 ]
 * @Author Tang
 * @Date 2020/4/5 16:22
 * @Created by ASUS
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userService;

    public JwtAuthenticationProvider(UserDetailsService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String token = ((JwtAuthenticationToken) authentication).getToken();

        // 解析token
        DecodedJWT decodedJWT = JwtHelper.parseJwtFrame(token);

        // token解析失败了
        if (decodedJWT == null) {
            throw new BadCredentialsException("JWT token verify fail");
        }
        // 解析成功 取出用户名
        String username = decodedJWT.getClaim("user").asString();

        // 根据用户名获取用户信息
        UserDetails user = userService.loadUserByUsername(username);

        // 用户信息查询失败了
        if (user == null || user.getPassword() == null) {
            throw new NonceExpiredException("Token expires");
        }

        // 返回验证成功的用户信息
        return new JwtAuthenticationToken(user, token, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }



}