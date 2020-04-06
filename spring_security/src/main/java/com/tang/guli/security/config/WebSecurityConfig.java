package com.tang.guli.security.config;

import com.tang.guli.security.filter.JsonLoginConfigurer;
import com.tang.guli.security.filter.JwtLoginConfigurer;
import com.tang.guli.security.filter.JwtRefreshSuccessHandler;
import com.tang.guli.security.filter.OptionsRequestFilter;
import com.tang.guli.security.handler.AjaxAccessDeniedHandler;
import com.tang.guli.security.handler.AjaxAuthenticationEntryPoint;
import com.tang.guli.security.handler.CustomerLogoutHandler;
import com.tang.guli.security.handler.LoginSuccessHandler;
import com.tang.guli.security.service.JwtAuthenticationProvider;
import com.tang.guli.security.service.JwtUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Classname WebSecurityConfig
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/4 9:06
 * @Created by ASUS
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AjaxAccessDeniedHandler ajaxAccessDeniedHandler;

    @Resource
    private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;

    @Resource
    private JwtUserDetailsService jwtUserDetailsService;

    @Resource
    private CustomerLogoutHandler customerLogoutHandler;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultPasswordEncoder defaultPasswordEncoder;

    //配置provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(
                new JwtAuthenticationProvider(jwtUserDetailsService));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略一些路径
        web.ignoring().mvcMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 启用跨域
        http.cors();
        // 返回给浏览器的Response的Header也需要添加跨域配置：
        http.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                //支持所有源的访问
                new Header("Access-control-Allow-Origin", "*"),
                //使ajax请求能够取到header中的jwt token信息
                new Header("Access-Control-Expose-Headers", "token"))));

        // 关闭 csrf 效验
        http.csrf().disable();

        // 对指定url进行角色验证
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                                                    // 数据库中role表的name字段 要以ROLE_开头！！ROLE_admin
                .antMatchers("/admin/**").hasAnyRole("admin","user","visitor")
                .antMatchers("/user/**").hasAnyRole("user","visitor")
                .antMatchers("/visitor/**").hasAnyRole("visitor")
                .anyRequest().authenticated(); // 其他请求访问都需要一个角色

        // 没有认证时访问触发
        http.httpBasic()
                .authenticationEntryPoint(ajaxAuthenticationEntryPoint);

        //没有权限访问资源时触发
        http.exceptionHandling()
                .accessDeniedHandler(ajaxAccessDeniedHandler);

        //禁用表单登录
        http.formLogin().disable();

        // 添加跨域过滤器
        http.addFilterAfter(new OptionsRequestFilter(), CorsFilter.class);

        http.apply(new JsonLoginConfigurer<>())
                .loginSuccessHandler(new LoginSuccessHandler(stringRedisTemplate))
                .and()
                .apply(new JwtLoginConfigurer<>())
                .tokenValidSuccessHandler(new JwtRefreshSuccessHandler(jwtUserDetailsService,stringRedisTemplate))
                .permissiveRequestUrls("/logout");

        //退出登录
        http.logout()
                . logoutRequestMatcher(new AntPathRequestMatcher("/loginOut", "GET"))
                .addLogoutHandler(customerLogoutHandler);
    }

    @Override
    public void setAuthenticationConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        super.setAuthenticationConfiguration(authenticationConfiguration);
    }

    @Bean("daoAuthenticationProvider")
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        // 自定义的密码
        daoProvider.setPasswordEncoder( defaultPasswordEncoder );
        // 自定义的 UserDetailsService
        daoProvider.setUserDetailsService(jwtUserDetailsService);
        return daoProvider;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD", "OPTION"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}