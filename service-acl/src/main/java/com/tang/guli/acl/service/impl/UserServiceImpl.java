package com.tang.guli.acl.service.impl;

import com.tang.guli.acl.domain.User;
import com.tang.guli.acl.mapper.UserMapper;
import com.tang.guli.acl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-04-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
