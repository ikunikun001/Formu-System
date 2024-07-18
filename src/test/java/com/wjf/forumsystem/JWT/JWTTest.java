package com.wjf.forumsystem.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {
    //生成token
    @Test
    public void testJWT(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","zhansan");
        map.put("age",18);
        String token=JWT.create()
                .withClaim("user",map)//荷载
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))// 添加过期时间
                .sign(Algorithm.HMAC256("wjf666"));//指定该算法，配置密钥
        System.out.println(token);
    }
    //解析token
    @Test
    public void testParse(){
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7Im5hbWUiOiJ6aGFuc2FuIiwiYWdlIjoxOH0sImV4cCI6MTcyMTE4MzE4MH0" +
                ".0LnfZRRGY2pQIQLAbGa5buCax23XqosKYfJqZ7xhOvs";
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("wjf666")).build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);//验证token,生成一个解析后的JWT对象
        Map<String, Claim> claim=decodedJWT.getClaims();
        System.out.println(claim.get("user"));

    }
}
