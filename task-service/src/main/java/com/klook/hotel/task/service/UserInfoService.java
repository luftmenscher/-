package com.klook.hotel.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.klook.hotel.task.mybatis.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserInfoService extends IService<UserInfo> {

    Boolean register(UserInfo userInfo);

    String login(UserInfo userInfo);

    List<UserInfo> getUserInfoList();

    String changePassword(String password,HttpServletRequest request);

}
