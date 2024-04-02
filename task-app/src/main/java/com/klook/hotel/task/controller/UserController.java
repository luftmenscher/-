package com.klook.hotel.task.controller;

import com.klook.hotel.task.config.Result;
import com.klook.hotel.task.mybatis.entity.UserInfo;
import com.klook.hotel.task.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody UserInfo userInfo) {

        return Result.success(userInfoService.register(userInfo));
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserInfo userInfo){
        return Result.success(userInfoService.login(userInfo));
    }

    @GetMapping("/list")
    public Result<List<UserInfo>> getUserInfoList(){
        return Result.success(userInfoService.getUserInfoList());
    }

    @PostMapping("/changePassword")
    public Result<String> changePassword(String password, HttpServletRequest request){
        return Result.success(userInfoService.changePassword(password,request));
    }

}
