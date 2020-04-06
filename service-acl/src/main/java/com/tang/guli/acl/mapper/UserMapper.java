package com.tang.guli.acl.mapper;

import com.tang.guli.acl.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author tang
 * @since 2020-04-03
 */
public interface UserMapper extends BaseMapper<User> {

    // 从数据库中取出用户信息
    User selectByUsername(String username);
}
