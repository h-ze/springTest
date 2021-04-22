package com.hz.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {

    /**
     * 添加jwt过期时间到redis中
     * @param compact 添加到redis中的key
     * @param timeout 过期时间
     * @return 是否添加到Redis成功
     */
    public boolean setRedisExpire(String compact,long timeout){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(compact,compact);
        redisTemplate.expire(compact, timeout, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.hasKey(compact);
        System.out.println(aBoolean);
        return aBoolean;
    }

    public boolean deleteRedisExpire(String userId){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        Boolean aBoolean =false;
        /*redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(compact,compact);
        redisTemplate.expire(compact, timeout, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.hasKey(compact);*/
        System.out.println(aBoolean);
        return aBoolean;
    }
}
