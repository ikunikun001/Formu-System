package com.wjf.forumsystem.interceptors;

import com.wjf.forumsystem.utils.JwtUtils;
import com.wjf.forumsystem.utils.ThreadLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("Authorization");
        try {
            ValueOperations<String, String> op = redisTemplate.opsForValue();
            String redisToken=op.get(token);
            if(redisToken==null){
                throw new RuntimeException();
            }
            Map<String,Object> cliams=JwtUtils.ParseToken(token);
            ThreadLocalUtils.set(cliams);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }
}
