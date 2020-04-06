package com.tang.guli.security.handler;

import com.tang.guli.security.utils.R;
import com.tang.guli.security.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname AjaxAuthenticationEntryPoint
 * @Description [ 身份没有验证的时候调用 ]
 * @Author Tang
 * @Date 2020/4/4 12:36
 * @Created by ASUS
 */
@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {

        System.out.println( "================身份没有验证===============" );

        ResponseUtil.out(httpServletResponse, R.ok().message("身份没有验证!"));
    }
}