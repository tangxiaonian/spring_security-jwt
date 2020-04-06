package com.tang.guli.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {

    public static void out(HttpServletResponse response, R r) {

        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-type","application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
