package com.tang.guli.security.handler;

import com.tang.guli.security.utils.R;
import com.tang.guli.security.utils.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.service.contexts.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CustomerLogoutHandler
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/4 22:18
 * @Created by ASUS
 */
@Component
public class CustomerLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        System.out.println( "=============登出============" );

        ResponseUtil.out(response, R.ok().message("登出成功!"));

    }
}