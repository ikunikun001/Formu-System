package com.wjf.forumsystem.controller;

import com.wjf.forumsystem.entity.Result;
import com.wjf.forumsystem.entity.User;
import com.wjf.forumsystem.service.UserService;
import com.wjf.forumsystem.utils.BytesToHex;
import com.wjf.forumsystem.utils.JwtUtils;
import com.wjf.forumsystem.utils.ThreadLocalUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @PostMapping("/register")
    public Result<String> register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password, @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$") String email){
        //查询是否有此用户名
        User user=userService.findByUsername(username);
        // 查询邮箱是否被占用
        User euser=userService.findByEmail(email);
        if(user==null&&euser==null){
            //没被占用
            userService.register(username,password,email);
            return Result.success();
        }else if(user!=null){
            return Result.error("用户名被占用！");
        }else if(euser!=null){
            return Result.error("邮箱被占用！");
        }
        return null;
    }
    @PostMapping("/login")
    // 登录
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        // 查询用户名
        User loginUser=userService.findByUsername(username);
        //用户存不存在
        if(loginUser==null){
            return Result.error("用户名不存在！");
        }
        // 用户存在，匹配密码
        String hashPassword=BytesToHex.getHash(password);
        if(hashPassword.equals(loginUser.getPassword())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("userId",loginUser.getUserId());
            claims.put("username",loginUser.getUsername());
            //携带令牌
            String token=JwtUtils.getToken(claims);
            // 将令牌存入redis
            ValueOperations<String, String> op = redisTemplate.opsForValue();
            op.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        // 密码错误
        return Result.error("密码错误！");
    }

    @GetMapping("/userInfo")
    // 获取用户信息
    public Result<User> getUserInfo(){
        Map<String,Object> claims=ThreadLocalUtils.get();
        String username= (String) claims.get("username");
        User user=userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success("修改成功");

    }

    @PatchMapping("/updateAvatar")
    public Result<String> updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatat(avatarUrl);
        return Result.success("修改成功");
    }

    @PatchMapping("/updatePwd")
    public Result<String> updatePad(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        String oldpassword=params.get("oldPwd");
        String newpassword=params.get("newPwd");
        String repassword=params.get("rePwd");
        if(!StringUtils.hasLength(newpassword) || !StringUtils.hasLength(oldpassword) || !StringUtils.hasLength(repassword)){
            return Result.error("缺少必要参数");
        }
        if(!repassword.equals(newpassword)){
            return Result.error("输入的两次密码不一致");
        }
        userService.updatePwd(oldpassword,newpassword);
        ValueOperations<String, String> op = redisTemplate.opsForValue();
        op.getOperations().delete(token);
        return Result.success("修改成功!");
    }
}
