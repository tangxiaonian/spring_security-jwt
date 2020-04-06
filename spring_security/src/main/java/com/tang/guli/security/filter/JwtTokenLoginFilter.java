package com.tang.guli.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tang.guli.security.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Classname JwtTokenLoginFilter
 * @Description [ 认证过滤器 ]
 * @Author Tang
 * @Date 2020/4/4 15:46
 * @Created by ASUS
 */
public class JwtTokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    public JwtTokenLoginFilter() {
        super();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 由此方法触发下面的登录逻辑  父类是模板类
        super.doFilter(req, res, chain);
    }

    /**
     * 复写登录逻辑
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // json 方式提交请求 request是获取不到的
        System.out.println("登录流程进来了...........");

        User user = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String username = user.getUsername();

        String password = user.getPassword();

        System.out.println(username + "--->" + password);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        // 包装
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}