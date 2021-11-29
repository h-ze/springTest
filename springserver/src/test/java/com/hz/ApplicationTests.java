package com.hz;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
//测试类在Test下的包名和java下的包名需要一致 否则会报错 例如 main下的包名叫com.hz test下的包名也应该叫com.hz
//@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    public void contextLoads() {
//        Calendar instance = Calendar.getInstance();
//        instance.add(Calendar.SECOND,90);
//        String token = JWT.create()
//                .withClaim("userId", 21)
//                .withClaim("username", "heze")
//                .withExpiresAt(instance.getTime())
//                .sign(Algorithm.HMAC256("123"));
//        System.out.println(token);
//        try {
//            System.out.println("获取的数据库连接为:"+dataSource.getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

    @Test(timeout = 1000)
    public void test(){
        System.out.println("1");
        /*JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify("");
        decodedJWT.getClaim("userId").asInt();*/
    }





}
