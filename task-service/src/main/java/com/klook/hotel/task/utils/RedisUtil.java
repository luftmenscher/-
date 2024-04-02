package com.klook.hotel.task.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public long ttl(String key){
        return redisTemplate.getExpire(key);
    }

    public void expire(String key,long timeout){
        redisTemplate.expire(key,timeout, TimeUnit.SECONDS);
    }

    public void del(String key){
        redisTemplate.delete(key);
    }

    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,String value,long timeout){
        redisTemplate.opsForValue().set(key,value,timeout,TimeUnit.SECONDS);
    }


    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }


}
