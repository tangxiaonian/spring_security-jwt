package com.tang.guli.security.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Classname UserSecurity
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/4 12:43
 * @Created by ASUS
 */
@Data
public class UserSecurity implements UserDetails {

    //当前登录用户
    private transient User currentUserInfo;

    //当前权限
    private List<String> permissionValueList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        permissionValueList.forEach((item) -> {

            collection.add(new SimpleGrantedAuthority(item));

        });


        return collection;
    }

    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}