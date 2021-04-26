package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class ApplicationTests {

    //@Test
    void contextLoads() {
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.SECOND,90);
//        String token = JWT.create()
//                .withClaim("userId", 21)
//                .withClaim("username", "heze")
//                .withExpiresAt(instance.getTime())
//                .sign(Algorithm.HMAC256("123"));
//        System.out.println(token);
        try {
            System.out.println("获取的数据库连接为:"+dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //@Test
    public void test(){
        /*JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify("");
        decodedJWT.getClaim("userId").asInt();*/
    }

    @Autowired
    DataSource dataSource;



}
