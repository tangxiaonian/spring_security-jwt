package com.tang.guli.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tang.guli.acl.domain.Role;
import com.tang.guli.acl.domain.User;
import com.tang.guli.acl.domain.UserRole;
import com.tang.guli.acl.mapper.RoleMapper;
import com.tang.guli.acl.mapper.UserMapper;
import com.tang.guli.acl.mapper.UserRoleMapper;
import com.tang.guli.security.config.DefaultPasswordEncoder;
import com.tang.guli.security.service.JwtUserDetailsService;
import com.tang.guli.utils.JwtHelper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Classname UserDetailsServiceImpl
 * @Description [ 查询用户信息  ]
 * @Author Tang
 * @Date 2020/4/4 10:06
 * @Created by ASUS
 */
@Component
public class UserDetailsServiceImpl implements JwtUserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 返回角色列表 对应前面配置的
        Collection<SimpleGrantedAuthority> roleList = new ArrayList<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", username);
        // 用户信息
        User user = userMapper.selectOne(queryWrapper);

        // 是管理员
        if ("admin".equals(username)) {

            List<Role> roles = roleMapper.selectList(null);

            roles.forEach(item -> {
                roleList.add(new SimpleGrantedAuthority(item.getRoleName()));
            });

        } else {
            // 其他用户
            QueryWrapper<UserRole> queryWrapper_ = new QueryWrapper<>();

            queryWrapper_.eq("user_id", user.getId());
            // 角色列表
            List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper_);

            // 获取角色信息
            userRoles.forEach(item -> {
                Role role = roleMapper.selectById(item.getRoleId());
                roleList.add(new SimpleGrantedAuthority(role.getRoleName()));
            });

        }

        roleList.forEach(System.out::println);

        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(), roleList);
    }

    @Override
    public String saveUserLoginInfo(UserDetails userDetails) {

        Map<String, Object> map = new HashMap<>(16);

        String username = userDetails.getUsername();

        map.put("username", username);

        // 角色列表缓存到redis中
        userDetails.getAuthorities().forEach(item -> {
            stringRedisTemplate.opsForSet().add("ROLE:" + username, item.getAuthority());
        });

        // 创建token返回

        return JwtHelper.createJwt(map, 3600);

    }


}