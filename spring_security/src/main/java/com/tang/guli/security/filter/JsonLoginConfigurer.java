package com.tang.guli.security.filter;

import com.tang.guli.security.handler.LoginFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

/**
 * @Classname JsonLoginConfigurer
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/5 15:27
 * @Created by ASUS
 */
public class JsonLoginConfigurer<T extends JsonLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
        extends AbstractHttpConfigurer<T, B> {

    private JwtTokenLoginFilter tokenLoginFilter;

    public JsonLoginConfigurer() {
        this.tokenLoginFilter = new JwtTokenLoginFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        tokenLoginFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置失败的Handler
        tokenLoginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        //不将认证后的context放入session
        tokenLoginFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        JwtTokenLoginFilter filter = postProcess(tokenLoginFilter);
        //指定Filter的位置
        http.addFilterAfter(filter, LogoutFilter.class);
    }
    //设置成功的Handler，这个handler定义成Bean，所以从外面set进来
    public JsonLoginConfigurer<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
        tokenLoginFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}