package com.tang.guli.security.handler;

import com.tang.guli.security.utils.R;
import com.tang.guli.security.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname AjaxAccessDeniedHandler
 * @Description [ 没有权限访问资源时调用 ]
 * @Author Tang
 * @Date 2020/4/4 12:38
 * @Created by ASUS
 */
@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException, ServletException {

        System.out.println( "================没有权限访问资源===============" );

        ResponseUtil.out(httpServletResponse, R.ok().message("没有权限访问资源!"));
    }
}