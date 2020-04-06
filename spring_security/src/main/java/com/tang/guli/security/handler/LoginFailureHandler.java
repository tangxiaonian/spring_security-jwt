package com.tang.guli.security.handler;

import com.tang.guli.security.utils.R;
import com.tang.guli.security.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname LoginFailureHandler
 * @Description [ 登录失败 ]
 * @Author Tang
 * @Date 2020/4/4 9:47
 * @Created by ASUS
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {

        System.out.println( "================登录失败===============" );

        ResponseUtil.out(httpServletResponse, R.error().message(e.getMessage()));

    }
}