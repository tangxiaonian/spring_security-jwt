package com.tang.guli.acl.controller;


import com.tang.guli.acl.domain.Permission;
import com.tang.guli.acl.service.PermissionService;
import com.tang.guli.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author tang
 * @since 2020-04-03
 */
@Api("权限接口")
@RestController
@RequestMapping("/acl/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionServiceImpl;

    @ApiOperation("获取所有的权限")
    @GetMapping("/all")
    public R selectAllPermission() {

        List<Permission> permissions = permissionServiceImpl.selectAllPermission();

        return R.ok().data(permissions);
    }

    @ApiOperation("删除菜单及子菜单")
    @GetMapping("/id")
    public R deleteMenuAndChildMenu(String id) {

        permissionServiceImpl.deleteMenuAndChildMenu(id);

        return R.ok();
    }

}

