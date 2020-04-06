package com.tang.guli.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tang.guli.acl.domain.UserRole;
import com.tang.guli.acl.mapper.UserRoleMapper;
import com.tang.guli.acl.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Classname UserRoleServiceImpl
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/3 21:57
 * @Created by ASUS
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}