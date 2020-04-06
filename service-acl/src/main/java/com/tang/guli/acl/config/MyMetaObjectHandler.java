package com.tang.guli.acl.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Classname MyMetaObjectHandler
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/3/26 13:59
 * @Created by ASUS
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        System.out.println( "****************fill********************" );

        this.setFieldValByName("gmtCreate", new Date(), metaObject);

        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}