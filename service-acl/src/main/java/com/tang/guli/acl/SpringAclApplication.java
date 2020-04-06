package com.tang.guli.acl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname SpringAclApplication
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/3 19:24
 * @Created by ASUS
 */
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.tang.guli"})
@MapperScan(value = {"com.tang.guli.acl.mapper"})
public class SpringAclApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringAclApplication.class, args);

    }

}