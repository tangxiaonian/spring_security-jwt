package com.tang.acl;

import com.tang.guli.acl.SpringAclApplication;
import com.tang.guli.acl.domain.Permission;
import com.tang.guli.acl.service.PermissionService;
import com.tang.guli.security.config.DefaultPasswordEncoder;
import com.tang.guli.security.utils.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname TestSpring
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/3 20:24
 * @Created by ASUS
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringAclApplication.class})
public class TestSpring {

    @Resource
    private PermissionService permissionService;

    @Resource
    private DefaultPasswordEncoder defaultPasswordEncoder;

    @Test
    public void test01(){

        List<Permission> permissions = permissionService.selectAllPermission();

        System.out.println(permissions);
    }

    @Test
    public void test03(){

        System.out.println(defaultPasswordEncoder.encode("123"));

        System.out.println(defaultPasswordEncoder.matches( "123","202cb962ac59075b964b07152d234b70"));

    }

    @Test
    public void test02(){

       permissionService.deleteMenuAndChildMenu("1195268616021139457");

    }


}