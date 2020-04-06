package com.tang.guli.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tang.guli.security.config.JwtAuthenticationToken;
import com.tang.guli.security.service.JwtUserDetailsService;
import com.tang.guli.utils.JwtHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Classname JwtRefreshSuccessHandler
 * @Description [ 登录成功，判断是否需要刷新token ]
 * @Author Tang
 * @Date 2020/4/5 16:34
 * @Created by ASUS
 */
public class JwtRefreshSuccessHandler implements AuthenticationSuccessHandler {

    //刷新间隔5分钟
    private static final int tokenRefreshInterval = 300;

    private JwtUserDetailsService jwtUserDetailsService;

    private StringRedisTemplate stringRedisTemplate;

    public JwtRefreshSuccessHandler(JwtUserDetailsService jwtUserDetailsService,
                                    StringRedisTemplate stringRedisTemplate) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("===========是否需要刷新token......");

        String token = ((JwtAuthenticationToken) authentication).getToken();
        // 解析jwt
        DecodedJWT decodedJWT = JwtHelper.parseJwtFrame(token);
        // token 是否需要刷新
        boolean shouldRefresh = (decodedJWT == null || shouldTokenRefresh(decodedJWT.getExpiresAt()));

        if (shouldRefresh) {
            // 获取新的token
            String newToken = jwtUserDetailsService.saveUserLoginInfo((UserDetails) authentication.getPrincipal());
            response.setHeader("token", newToken);
        }

    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        // 现在的系统时间 - 5分钟是否小于 jwt 中的时间
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

}