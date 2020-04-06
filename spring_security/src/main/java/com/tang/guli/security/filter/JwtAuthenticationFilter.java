package com.tang.guli.security.filter;

import com.tang.guli.security.config.JwtAuthenticationToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname JwtAuthenticationFilter
 * @Description [ 定义拦截器，拦截用户登录请求，进行认证token处理 ]
 * @Author Tang
 * @Date 2020/4/5 15:40
 * @Created by ASUS
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;

    private List<RequestMatcher> permissiveRequestMatchers;

    private AuthenticationManager authenticationManager;

    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    public JwtAuthenticationFilter() {
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("token");
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
    }

    protected String getJwtToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //header没带token的，直接放过，因为部分url匿名用户也可以访问

        //如果需要不支持匿名用户的请求没带token，这里放过也没问题，

        // 因为SecurityContext中没有认证信息，后面会被权限控制模块拦截
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authResult = null;
        AuthenticationException failed = null;
        try {
            String token = getJwtToken(request);
            System.out.println( token );
            // token 存在
            if (StringUtils.isNotBlank(token)) {

                // 封装token
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);

                // 封装后提交给AuthenticationManager
                authResult = this.getAuthenticationManager().authenticate(authToken);

            } else {

                failed = new InsufficientAuthenticationException("JWT is Empty");

            }
        }  catch (InternalAuthenticationServiceException e) {

            logger.error("An internal error occurred while trying to authenticate the user.", failed);

            failed = e;

        } catch (AuthenticationException e) {
            // Authentication failed
            failed = e;

        }
        if (authResult != null) {

            // token认证成功
            successfulAuthentication(request, response, filterChain, authResult);

        } else if (!permissiveRequest(request)) {

            // token认证失败
            unsuccessfulAuthentication(request, response, failed);

            return;
        }
        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null)
            return false;
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request))
                return true;
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (permissiveRequestMatchers == null)
            permissiveRequestMatchers = new ArrayList<>();
        for (String url : urls)
            permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
    }

    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    protected AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    protected AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }
}