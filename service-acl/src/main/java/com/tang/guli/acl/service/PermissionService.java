package com.tang.guli.acl.service;

import com.tang.guli.acl.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author tang
 * @since 2020-04-03
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取所有资源列表
     * @return
     */
    List<Permission> selectAllPermission();

    /**
     * 删除菜单及子菜单
     * @param id
     */
    void deleteMenuAndChildMenu(String id);

    /**
     * 保存角色权限关联关系
     * @param role
     * @param pIds
     */
    void saveRolePermissionRelationShip(String role,String[] pIds);
}
