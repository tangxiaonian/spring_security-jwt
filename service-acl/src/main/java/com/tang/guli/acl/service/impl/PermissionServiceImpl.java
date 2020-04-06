package com.tang.guli.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tang.guli.acl.domain.Permission;
import com.tang.guli.acl.domain.RolePermission;
import com.tang.guli.acl.mapper.PermissionMapper;
import com.tang.guli.acl.service.PermissionService;
import com.tang.guli.acl.service.RolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-04-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public List<Permission> selectAllPermission() {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pid", "0");
        // 先查询第一个列表
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);

        buildPermissionMenu(permissionList,0);

        return permissionList;
    }

    /**
     * 构建子菜单
     * @param permissions
     * @return
     */
    private List<Permission> buildPermissionMenu(List<Permission> permissions,int level) {

        permissions.forEach((permission -> {
            // 设置等级
            permission.setLevel(level);

            QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
            // 创建查询pid为当前id的条件
            queryWrapper.eq("pid", permission.getId());
            // 查询
            List<Permission> permissionList = permissionMapper.selectList(queryWrapper);
            //递归查询
            if (permissionList != null) {
                permission.setPermissions(buildPermissionMenu(permissionList,level + 1));
            }
        }));

        return permissions;
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void deleteMenuAndChildMenu(String id) {

        List<String> ids = new ArrayList<>();
        // 递归查询列表id
        this.selectMenuAndChildMenuId(id,ids);

        ids.add(id);

        System.out.println( ids );

    }

    private void selectMenuAndChildMenuId(String id,List<String> ids) {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pid", id);

        queryWrapper.select("id");

        List<Permission> permissions = permissionMapper.selectList(queryWrapper);

        permissions.forEach(item->{

            ids.add(item.getId());
            // 递归查询放入列表中
            selectMenuAndChildMenuId(item.getId(),ids);

        });

    }

    /**
     * 分配角色资源
     * @param role
     * @param pIds
     */
    @Override
    public void saveRolePermissionRelationShip(String role, String[] pIds) {

        List<RolePermission> rolePermissionList = new ArrayList<>();

        Arrays.stream(pIds).forEach((pId)->{

            RolePermission rolePermission = new RolePermission();

            rolePermission.setRoleId(role);

            rolePermission.setPermissionId(pId);

            rolePermissionList.add(rolePermission);
        });

        rolePermissionService.saveBatch(rolePermissionList);
    }
}
