package com.tang.guli.acl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TestAdminController
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/4 13:51
 * @Created by ASUS
 */
@RestController
@RequestMapping("/visitor")
public class TesVisitorController {

    @GetMapping("/index")
    public Map<String, String> index() {

        Map<String, String> map = new HashMap<>(16);

        map.put("index", "success");

        return map;
    }

}