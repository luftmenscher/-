package com.klook.hotel.task.service.impl;


import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klook.hotel.task.mybatis.entity.UserInfo;
import com.klook.hotel.task.mybatis.mapper.UserInfoMapper;
import com.klook.hotel.task.service.UserInfoService;
import com.klook.hotel.task.utils.JwtUtil;
import com.klook.hotel.task.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public Boolean register(UserInfo userInfo) {

        List<UserInfo> selectedList = list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, userInfo.getUsername()));
        if (!selectedList.isEmpty()) {
            throw new RuntimeException("注册失败，该用户名已存在");
        }
        // 使用MD5对密码加密
        String encodedPassword= SecureUtil.md5(userInfo.getPassword());
        userInfo.setPassword(encodedPassword);
        return save(userInfo);
    }

    @Override
    public String login(UserInfo userInfo) {
        List<UserInfo> selectedList = list(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, userInfo.getUsername()));
        if (selectedList.isEmpty()) {
            throw new RuntimeException("登录失败，账号不存在");
        }
        UserInfo selected = selectedList.get(0);
        String encodedPassword= selected.getPassword();
        String inputEncodePassword= SecureUtil.md5(userInfo.getPassword());
        if (!encodedPassword.equals(inputEncodePassword)){
            throw new RuntimeException("登录失败，密码错误");
        }

        // 生成令牌
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("userId", selected.getId());
        String token = JwtUtil.generateToken(map);

        //将token存到redis
        redisUtil.set("user_"+selected.getId(),token,60*30);
        return token;
    }

    @Override
    public List<UserInfo> getUserInfoList() {
        return userInfoMapper.selectList(null);
    }

    @Override
    public String changePassword(String password, HttpServletRequest request) {

        // 从请求头里面读取token
        String token = request.getHeader("Authorization");
        // 解析令牌得到userId
        Integer userId = (Integer) JwtUtil.resolveToken(token).get("userId");

        UserInfo updateUserInfo=new UserInfo();
        updateUserInfo.setPassword(SecureUtil.md5(password));
        updateUserInfo.setId(userId);
        if (!updateById(updateUserInfo)){
            throw new RuntimeException("密码修改失败，请重试");
        }
        // 生成新令牌
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("userId", userId);
        String newToken = JwtUtil.generateToken(map);

        //更新redis中的token
        redisUtil.set("user_"+userId,newToken,60*30);
        return newToken;
    }
}
