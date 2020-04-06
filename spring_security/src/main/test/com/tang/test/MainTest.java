package com.tang.test;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tang.guli.utils.JwtHelper;
import org.junit.Test;

/**
 * @Classname MainTest
 * @Description [ TODO ]
 * @Author Tang
 * @Date 2020/4/5 23:11
 * @Created by ASUS
 */
public class MainTest {

    public static void main(String[] args) {

    }

    @Test
    public void test01(){

        String jwtFrame = JwtHelper.createJwtFrame("tang", 3600);

        DecodedJWT decodedJWT = JwtHelper.parseJwtFrame(jwtFrame);

        System.out.println( decodedJWT );

    }

}