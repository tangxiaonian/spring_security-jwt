package com.tang.guli.security.handler;

import com.tang.guli.security.utils.R;
import com.tang.guli.security.utils.ResponseUtil;
import com.tang.guli.utils.JwtHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname LoginFailureHandler
 * @Description [ 登录成功 ]
 * @Author Tang
 * @Date 2020/4/4 9:47
 * @Created by ASUS
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private StringRedisTemplate stringRedisTemplate;

    public LoginSuccessHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("===============登录成功============");

        // 取出封装登录信息的对象
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        // 角色列表缓存到redis中
        userDetails.getAuthorities().forEach(item -> {
            stringRedisTemplate.opsForSet().add("ROLE:" + username, item.getAuthority());
        });


        // 创建token返回
        String token = JwtHelper.createJwtFrame(username, 3600);

        ResponseUtil.out(httpServletResponse, R.ok().message("登录成功!").data(token));
    }
}