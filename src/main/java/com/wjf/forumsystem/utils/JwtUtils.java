package com.wjf.forumsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    //生成token
    private static final String KEY="wjf666";
    public static String getToken(Map<String,Object> claims){
        String token= JWT.create()
                .withClaim("claims",claims)//荷载
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60))// 添加过期时间
                .sign(Algorithm.HMAC256(KEY));//指定该算法，配置密钥
        return token;
    }
    //解析token
    public static Map<String,Object> ParseToken(String token){
        return JWT.require(Algorithm.HMAC256(KEY)).build().verify(token).getClaims().get("claims").asMap();

    }
}
