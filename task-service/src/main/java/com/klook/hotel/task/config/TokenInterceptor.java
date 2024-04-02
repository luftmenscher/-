package com.klook.hotel.task.config;

import com.klook.hotel.task.utils.JwtUtil;
import com.klook.hotel.task.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TokenInterceptor implements HandlerInterceptor {

    /**
     * 请求头
     */
    private static final String HEADER_AUTH = "Authorization";

    /**
     * 安全的url，不需要令牌
     */
    private static final List<String> SAFE_URL_LIST = Arrays.asList("/user/login", "/user/register");

    @Autowired
    private RedisUtil redisUtil;

    public TokenInterceptor(RedisUtil redisUtil){
        this.redisUtil=redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setContentType("application/json; charset=utf-8");


        String url = request.getRequestURI().substring(request.getContextPath().length());
//        System.out.println(url);

        // 登录和注册等请求不需要令牌
        if (SAFE_URL_LIST.contains(url)) {
            return true;
        }

        // 从请求头里面读取token
        String token = request.getHeader(HEADER_AUTH);
        // 解析令牌
        Map<String, Object> map = JwtUtil.resolveToken(token);
        String userId = "user_"+map.get("userId").toString();
//        String token1=redisUtil.get(userId);
//        System.out.println("token:"+token);
//        System.out.println("token1:"+token1);
        if (redisUtil.get(userId)==null || !redisUtil.get(userId).equals(token)){
            throw new RuntimeException("请求失败，请先登录");
        }

        //自动续期token
        if (redisUtil.ttl(userId)<60*5){
            redisUtil.set(userId,token,60*30);
        }

        return true;
    }

}
