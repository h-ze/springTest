package com.hz.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class JWTUtil {
    private String key ;
    private long ttl ;//一个小时
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getTtl() {
        return ttl;
    }
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }


    public String createJWT(String id, String subject,String password, String roles) {
        long nowMillis = System.currentTimeMillis();

        JwtBuilder builder= Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"itcast")
                .setExpiration(new Date(nowMillis+1000*60 *60 *24))
                //.claim("username",username)
                .claim("password",password)
                .claim("roles",roles)
                ;
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        String compact = builder.compact();
        redisTemplate.opsForValue().set(compact,compact);
        System.out.println(compact);
        redisTemplate.expire(compact, 60, TimeUnit.SECONDS);
        Boolean aBoolean = redisTemplate.hasKey(compact);
        System.out.println(aBoolean);
        return compact;
    }

    public static Claims parseJWT(String jwtStr){
        System.out.println(jwtStr);
        return        Jwts.parser().setSigningKey("itcast").parseClaimsJws(jwtStr).getBody(
        );
    }
}
