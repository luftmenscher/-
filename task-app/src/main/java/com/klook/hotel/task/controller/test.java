package com.klook.hotel.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class test {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/get-users")
    public List<Map<String,Object>> getUserList(){
        String sql="select * from user";
        List<Map<String,Object>> res=jdbcTemplate.queryForList(sql);
        System.out.println("aaaaa");
        return res;
    }
}
