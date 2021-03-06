package com.tang.guli.security.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @Classname JwtAuthenticationToken
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/5 15:58
 * @Created by ASUS
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 3981518947978158945L;
    private UserDetails principal;
    private String credentials;
    private String token;
    public JwtAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
    }
    public JwtAuthenticationToken(UserDetails principal, String token,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }
    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return credentials;
    }
    @Override
    public Object getPrincipal() {
        return principal;
    }
    public String getToken() {
        return token;
    }
}